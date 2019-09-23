# SaltBlock
## Usage
*SaltBlock is currently under development and isn't necessarily stable*, thus it is not available
as a gradle dependency, nor is a jar available. You will have to clone the repo and compile the
jar on your own.

SaltBlock is designed to return encrypted strings to be written to a database or the cloud.

```java
// no parameter passed to the constructor defaults to AES encryption
SaltBlock saltBlock = new SaltBlock();
String encryptedStr = saltBlock.encrypt("myUniqueKeyAlias", "My message to encrypt");

String decryptedStr = saltBlock.decrypt("myUniqueKeyAlias", encryptedStr);
```