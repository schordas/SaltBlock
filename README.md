# SaltBlock
## Purpose
The purpose of SaltBlock is to provide an easy to use library that allows developers to leverage
encryption to secure user data. There are many uses for encryption of course, but the original use
case is to encrypt your user's input before storing it in the cloud, or even locally. SaltBlock has
implemented both public and private key encryption to allow for sharing encrypted data and for
keeping it private to the user.

The ultimate goal is to protect users from data breaches, leaks, data analytics, ads, and potentially
surveillance.

## Usage
*SaltBlock is currently under development and isn't necessarily stable. Furthermore, it has
not yet been sufficiently tested for best practices with key sharing and key generation*, thus it is
not available as a gradle dependency, nor is a jar available. Once the library has been thoroughly
tested it will be more readily available. For now you will have to clone the repo and compile the
jar on your own.

As such, the sample app **note** is not necessary to build the library. Note is only for
demonstration purposes and will soon be replaced with a different sample.

SaltBlock is designed to return encrypted strings to be written to a database or the cloud. A brief
example would look something like this:

```java
// no parameter passed to the constructor defaults to AES encryption
SaltBlock saltBlock = new SaltBlock();
String encryptedStr = saltBlock.encrypt("myUniqueKeyAlias", "My message to encrypt");

String decryptedStr = saltBlock.decrypt("myUniqueKeyAlias", encryptedStr);
```

You can find much more detailed use cases in the wiki [examples](https://github.com/schordas/SaltBlock/wiki/Examples) and
[encryption](https://github.com/schordas/SaltBlock/wiki/Encryption-Methods)/[decryption](https://github.com/schordas/SaltBlock/wiki/Decryption-Methods)
documentation.