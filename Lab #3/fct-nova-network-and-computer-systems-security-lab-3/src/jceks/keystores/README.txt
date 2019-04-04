#
# /bin/sh!
# How to create a Key Store to save and keep Symmetric Secret Keys
#
# Instruction:
# - keytool -genseckey -alias <entry> -keyalg <alg> -keysize <size> -keystore <keystorename> -storetype jceks
# 
# NOTE:
# - By default, the keytool uses Key Stores JKS and this Key Stores are used to support asymmetric keys and
#   Public Key Algorithms or management of Public Keys' Certifications, in various, supported formats;
# 
# Examples in the case of, management of Symmetric Secret Keys:
# a) Generate and save a Secret Key ciphered by the AES (Advanced Encryption Standard - Rijndael) Algorithm,
#    with a 256-bits Secret Key Size, in the entry #1:
#    a.1) keytool -genseckey -alias mykey1 -keyalg aes -keysize 256 -keystore mykeystore.jceks -storetype jceks
# b) Generate and save a Secret Key ciphered by the Blowfish Algorithm,
#    with a 448-bits Secret Key Size, in the entry #2:
#    b.1) keytool -genseckey -alias mykey2 -keyalg blowfish -keysize 448 -keystore mykeystore.jceks -storetype jceks
# 
# For more information on the keytool see:
# - https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html
#