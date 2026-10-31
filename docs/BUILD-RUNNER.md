# Build and run iX OS with GitHub Actions

The workflow at
[`.github/workflows/build-and-run-ixos.yml`](../.github/workflows/build-and-run-ixos.yml)
builds the actual AOSP product and can boot the resulting image in Cuttlefish.
It is a manual workflow because a full AOSP build cannot fit on a standard
GitHub-hosted runner.

## Required self-hosted runner

The build runner must be a dedicated Linux x86-64 machine with:

- At least **64 GiB RAM**.
- At least **400 GiB free disk space** on the filesystem used for the AOSP
  checkout and build output.
- A case-sensitive Linux filesystem and a supported Ubuntu/Debian environment.
- Git, Python 3, unzip, and Google's Repo launcher (version 2.4 or newer).
- For the boot test: KVM enabled and accessible at `/dev/kvm`, plus the
  Cuttlefish host package installed.

These are AOSP's documented minimum hardware requirements. A normal
GitHub-hosted runner, an 8 GiB desktop, a Chromebook, or the Raspberry Pi 3 B+
is not suitable for this full build.

## Provision Cuttlefish on the Linux host

Follow Google's current
[Cuttlefish get-started guide](https://source.android.com/docs/devices/cuttlefish/get-started)
to install the host packages. The host must have the Cuttlefish base package
installed and configured before the workflow runs.

The runner account must be in the `kvm`, `cvdnetwork`, and `render` groups.
For example, when those groups and packages are installed:

```bash
sudo usermod -aG kvm,cvdnetwork,render YOUR_RUNNER_USERNAME
sudo reboot
```

The reboot is important: it applies the Cuttlefish host modules and device
rules. Restart the GitHub Actions runner service after the account's group
membership has changed. Confirm that the runner can read and write
`/dev/kvm` and that `cuttlefish-base` is installed.

## Register the GitHub Actions runner

In the repository, open **Settings → Actions → Runners → New self-hosted
runner**, select **Linux** and **x64**, and follow GitHub's displayed setup
instructions on the build host.

When configuring the runner, add this custom label:

```text
ixos-aosp
```

The workflow selects the runner with the labels:

```yaml
runs-on: [self-hosted, linux, x64, ixos-aosp]
```

Do not publish the temporary runner registration token. Use the token shown
by GitHub's runner setup page and install the runner as a service so that it is
available when a workflow is dispatched.

## Run the workflow

1. Open the repository's **Actions** tab.
2. Select **Build and run CGL+ iX OS**.
3. Select **Run workflow** on the `main` branch.
4. Keep `AOSP branch` as `android-latest-release` for the current AOSP
   release.
5. Leave **Boot the image in Cuttlefish** enabled to build, start the virtual
   device, wait for Android boot completion, install/check the iX launcher,
   start its HOME activity, and capture a screenshot.
6. Optionally enable **Upload the built image ZIP and Cuttlefish host package**
   to attach the large build outputs to the workflow run. It is disabled by
   default because Android image artifacts can consume substantial GitHub
   Actions storage.

The workflow first runs source/XML/shell syntax checks on GitHub-hosted
Ubuntu. The actual AOSP build runs only on the self-hosted `ixos-aosp` runner.
If no matching runner is online, the build job remains queued; the source
validation job may still pass.

## Build process and evidence

The build script:

1. Initializes or updates the AOSP `android-latest-release` source checkout.
2. Adds this repository as `device/cgl/ixos` using the local manifest and pins
   it to the exact commit that triggered the workflow.
3. Runs `repo sync`.
4. Selects `ixos_cf_x86_64-aosp_current-userdebug`.
5. Runs `m dist` to build the iX image and Cuttlefish host package.
6. When enabled, launches Cuttlefish with KVM and verifies Android boot,
   launcher package installation, and successful start of
   `com.cgl.ixos.launcher/.IXLauncherActivity`.

Boot evidence and logs are uploaded automatically. When image upload is
enabled, the image ZIP and host package are uploaded as a separate artifact.
Otherwise, the image remains in the runner's AOSP checkout under
`out/dist/`.

A successful build and a successful Cuttlefish boot are distinct results.
Neither one proves that the separate Raspberry Pi 3 B+ hardware port works.
Do not flash the Cuttlefish image onto a physical phone or Raspberry Pi.
