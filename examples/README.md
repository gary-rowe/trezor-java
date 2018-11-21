# Examples module

This module contains a collection of examples to assist developers in using the library.

It is suggested that those unfamiliar with the library or the device progress in the order
presented here. That way you will rapidly understand the asynchronous structure of the API 
and the reasons behind it.

## Ping

The `ping` message is stateless and can be sent at any time to a device to check that it is
still responsive. Consequently, it is outside the normal state machine operations. It is
the simplest expression of an asynchronous message.

## Features

The `initialize` message results in a `features` response which can be interrogated to uncover
the capabilities of the attached device.

It is also used to reset the overall state of the device back to an entry state.

## Wipe Device

The `wipe` message results in a factory reset of the device and is the simplest message that
involves multiple state transitions involving user confirmation and screen requests from the
application.

After a successful factory reset the `features` message is null.

## Create Wallet

