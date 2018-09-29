# Trezor Java

Integrate Trezor devices into your project using this MIT license library. 

### Project status

Build status: [![Build Status](https://api.travis-ci.org/gary-rowe/trezor-java.svg?branch=develop)](https://travis-ci.org/gary-rowe/trezor-java#)

Release status: Pre-alpha - use only in a research environment

### Technologies

* [usb4java](https://github.com/usb4java/usb4java) - Java library wrapping `libusb-1.0` for non-HID interfaces
* [Google Protocol Buffers](https://code.google.com/p/protobuf/) (protobuf) - for the most efficient and flexible wire protocol
* [Trezor Common](https://github.com/trezor/trezor-common) - for the protobuf code generation
* Java 8+ - to remove dependencies on JVMs that have reached end of life

### Code examples

Configure and start the hardware wallet service as follows:

TODO

### Getting started

#### Building locally

This project uses gradle (compatible with Maven) and will "self-install" at the project level if not already 
present on your machine. Before starting it is necessary to perform an initial build to generate the protobuf
source code files. Simply do the following:

```bash
$ cd <project directory>
$ ./gradlew clean build
```

and you're good to go. If you encounter problems, perhaps with compilation within an IDE, please review the Troubleshooting section below.

#### Run with an attached device

Each module has a simple command line interface to allow exercising attached devices. Access these as follows:

```bash
$ cd <project directory>
$ ./gradlew :core:run
$ ./gradlew :service:run
```

### Frequently asked questions (FAQ)

#### Which module should I use?

The `core` module is for low level access to the Trezor device. Essentially just basic message passing with no context or use
case in mind. Use this if you already have a service layer for your project and just want to communicate with a Trezor
with the bare minimum of dependencies.

The `service` module is for high level access to the Trezor device. It contains a collection of simple entry points that cover many 
common use cases for the Trezor device. Use this if you are integrating the Trezor device into an existing project. It relies on the
popular [Bitcoinj](https://github.com/bitcoinj/bitcoinj) library.

#### How do I include this in my project?

Maven Central is your friend here. Note that `service` will include `core`. 

Maven:
```xml
<dependencies>
  <1-- Core is included in Service -->
  <dependency>
    <groupId>uk.co.froot.trezorjava</groupId>
    <artifactId>core</artifactId>
    <version>0.0.1</version>
  </dependency>
  <dependency>
    <groupId>uk.co.froot.trezorjava</groupId>
    <artifactId>service</artifactId>
    <version>0.0.1</version>
  </dependency>
</dependencies>

```

Gradle:
```groovy
dependencies {

  // Core is included in service
  compile "uk.co.froot.trezorjava:core:0.0.1"
  compile "uk.co.froot.trezorjava:service:0.0.1"

}
```

#### What use cases do you support?

At present there is support and examples for the following high level use cases:

`core` module

* Open attached Trezor Model T device - Done
* Send/receive protobuf messages to attached device - Done

`service` - module

* Process device events (attachment, button press etc) - TODO
* Hotplug attachment/detachment detection - TODO
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
$ cd <project directory>/core
$ git submodule init
$ git submodule update
```
This will bring down the `.proto` files referenced in the submodules and allow you to select which tagged commit to use when generating
the protobuf files. See the "Updating protobuf files" section later.

Satoshi Labs (creators of the Trezor device) maintain the `.proto` files. As changes are reported this project will update their protobuf files through the following process: 

```bash
$ cd <project directory>/core/trezor-common
$ git checkout master
$ git pull origin master
$ cd ../..
$ ./gradlew clean build
$ git add core/trezor-common
$ git commit -m "Updating protobuf files for 'trezor-common'"
$ git push
```

Normally the HEAD of the submodule origin `master` branch is [the latest production release](http://nvie.com/posts/a-successful-git-branching-model/), but that's 
up to the owner of the upstream repo.

The generated source of the protobuf files are not held in version control. This is because they are derived resources and can run into several megabytes of code
that are likely to never be reviewed by a developer. This only serves to bloat the upstream repository to no advantage.

### Modules

The following section will help you become more familiar with the code base.

#### Core

This is the lowest level of the API. It provides a very simple abstraction of the Trezor devices in terms of their USB connectivity. 
It strongly relies on `usb4java`. Typically developers will use this to communicate with the Trezor device and then place a higher level
API above it.

The Core module contains the Trezor Common repository so that it can access the latest protobuf messages. A `TrezorDeviceManager` is used
as the primary entry point to the device and a `TrezorEventListener` interface is used as the callback entry point for any events from the
device. Note that the device is spontaneous in providing events such as a connect/disconnect.

#### Service

The service API provides a medium level of complexity and aims to support common use cases through a simple call to a service. This then
uses a finite state machine (FSM) to manage the ongoing interaction with the device and the messages passing to and from it. 

This is the normal entry level for developers wishing to use the Trezor to get something done.

#### Examples

The examples are there to provide a minimal set of code to achieve common use cases. They should not be assumed to be production level code
as they do not have all the error handling code that would require.

Typically an example will have a `main()` method and will stay running long enough for the device to exercised. In some cases there is a requirement
for use interactivity and this is done from the command line. 

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
