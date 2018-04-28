# Trezor Java

Integrate Trezor devices into your project using this MIT license library. 

### Project status

Build status: [![Build Status](https://api.travis-ci.org/gary-rowe/trezor-java.svg?branch=develop)](https://travis-ci.org/gary-rowe/trezor-java#)

Release status: Pre-alpha - use only in a research environment

### Technologies

* [usb4java](https://github.com/usb4java/usb4java) - Java library wrapping `libusb-1.0` for non-HID interfaces
* [Google Protocol Buffers](https://code.google.com/p/protobuf/) (protobuf) - for the most efficient and flexible wire protocol
* Java 8+ - to remove dependencies on JVMs that have reached end of life

### Code examples

Configure and start the hardware wallet service as follows:

TODO

### Getting started

This project uses gradle and can be used without having external hardware attached. Gradle comes with a 

```
$ cd <project directory>
$ ./gradlew clean build
```

and you're good to go. Your next step is to explore the examples (see later for details).

### Frequently asked questions (FAQ)

#### What use cases do you support ?

At present there is support and examples for the following high level use cases:

* Attachment/detachment detection - TODO
* Wipe device to factory settings - TODO
* Load wallet with known seed phrase (insecure) - TODO
* Create wallet on device with PIN and external entropy (secure) - TODO
* Request address using chain code - TODO
* Request public key using chain code - TODO
* Sign transaction (integrates with [Bitcoinj](http://bitcoinj.org) `Transaction`) - TODO
* Request cipher key (deterministically encrypt/decrypt based on a chain code) - TODO
* Sign message - TODO
* Change PIN - TODO
* Recover device - TODO 
* Upload firmware - TODO
* Verify message using chain code - TODO
* Encrypt/decrypt based on AES key - TODO

#### Collaborators and the protobuf files

If you are a collaborator (i.e. you have commit access to the repo) then you will need to perform an additional stage to ensure you have
the correct version of the protobuf files:

```
$ cd <project directory>
$ git submodule init
$ git submodule update
```
This will bring down the `.proto` files referenced in the submodules and allow you to select which tagged commit to use when generating
the protobuf files. See the "Updating protobuf files" section later.

Satoshi Labs (creators of the Trezor device) maintain the `.proto` files. As changes are reported this project will update their protobuf files through the following process: 

```bash
cd trezor-common
git checkout master
git pull origin master
cd ..
./gradlew clean build
git add trezor-common
git commit -m "Updating protobuf files for 'trezor-common'"
git push
```

Normally the HEAD of the submodule origin `master` branch is [the latest production release](http://nvie.com/posts/a-successful-git-branching-model/), but that's 
up to the owner of the upstream repo.

The generated source of the protobuf files are not held in version control. This is because they are derived resources and can run into several megabytes of code
that are likely to never be reviewed by a developer. This only serves to bloat the upstream repository to no advantage.

### Troubleshooting

The following are known issues and their solutions or workarounds.

#### I'm seeing `Device not connected` in the logs

Check that you don't have an open browser window to [Trezor.io](https://trezor.io) or perhaps another application that is holding a connection to the device open
through the Trezor Bridge and WebUSB.. 

#### Intellij doesn't import generated protobuf files like `TrezorManager`

This [Stackoverflow answer](https://stackoverflow.com/a/47411405/396747) provides good instructions:

```text

In IntelliJ IDEA 2016 and newer you can change this setting in Help > Edit Custom Properties.

On older versions, there's no GUI to do it. But you can change it if you edit the IntelliJ IDEA Platform Properties file as described here: https://intellij-support.jetbrains.com/hc/en-us/articles/206544869-Configuring-JVM-options-and-platform-properties

# Maximum file size (kilobytes) IDE should provide code assistance for.
idea.max.intellisense.filesize=50000

# Maximum file size (kilobytes) IDE is able to open.
idea.max.content.load.filesize=50000

Don't forget to save and restart IntelliJ IDEA.

```

### Closing notes

All trademarks and copyrights are acknowledged.
