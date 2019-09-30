# SaltBlock
## Usage
*SaltBlock is currently under development and isn't necessarily stable*, thus it is not available
as a gradle dependency, nor is a jar available. You will have to clone the repo and compile the
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