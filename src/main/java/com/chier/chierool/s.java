//import org.yaml.snakeyaml.DumperOptions;
//import org.yaml.snakeyaml.Yaml;
//
//import javax.crypto.Cipher;
//import java.io.*;
//import java.security.*;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//
//public class RSAEncryptionToYAML {
//
//    private static final String PUBLIC_KEY_FILE = "public.key";
//    private static final String PRIVATE_KEY_FILE = "private.key";
//    private static final String ENCRYPTED_DATA_FILE = "encrypted_data.yaml";
//
//    public static void main(String[] args) {
//        try {
//            // 生成密钥对或加载已有密钥对
//            KeyPair keyPair = generateOrLoadKeyPair();
//
//            // 加密数据并保存到 YAML 文件
//            String originalData = "Hello, World!";
//            byte[] encryptedData = encryptData(originalData, keyPair.getPublic());
//            saveEncryptedDataToYaml(encryptedData, ENCRYPTED_DATA_FILE);
//
//            // 从 YAML 文件中读取加密数据并解密
//            byte[] restoredEncryptedData = loadEncryptedDataFromYaml(ENCRYPTED_DATA_FILE);
//            String decryptedData = decryptData(restoredEncryptedData, keyPair.getPrivate());
//            System.out.println("Decrypted Data: " + decryptedData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static KeyPair generateOrLoadKeyPair() throws Exception {
//        KeyPair keyPair;
//        File privateKeyFile = new File(PRIVATE_KEY_FILE);
//        File publicKeyFile = new File(PUBLIC_KEY_FILE);
//
//        if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
//            keyPair = generateKeyPair();
//            saveKeysToFile(keyPair);
//        } else {
//            keyPair = loadKeysFromFile();
//        }
//        return keyPair;
//    }
//
//    private static KeyPair generateKeyPair() throws Exception {
//        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//        keyPairGenerator.initialize(2048);
//        return keyPairGenerator.generateKeyPair();
//    }
//
//    private static void saveKeysToFile(KeyPair keyPair) throws IOException {
//        try (ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
//             ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE))) {
//
//            publicKeyOS.writeObject(keyPair.getPublic());
//            privateKeyOS.writeObject(keyPair.getPrivate());
//        }
//    }
//
//    private static KeyPair loadKeysFromFile() throws IOException, ClassNotFoundException {
//        try (ObjectInputStream publicKeyIS = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
//             ObjectInputStream privateKeyIS = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE))) {
//
//            PublicKey publicKey = (PublicKey) publicKeyIS.readObject();
//            PrivateKey privateKey = (PrivateKey) privateKeyIS.readObject();
//            return new KeyPair(publicKey, privateKey);
//        }
//    }
//
//    private static byte[] encryptData(String data, PublicKey publicKey) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        return cipher.doFinal(data.getBytes());
//    }
//
//    private static String decryptData(byte[] encryptedData, PrivateKey privateKey) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] decryptedData = cipher.doFinal(encryptedData);
//        return new String(decryptedData);
//    }
//
//    private static void saveEncryptedDataToYaml(byte[] encryptedData, String filePath) throws IOException {
//        String base64EncodedData = Base64.getEncoder().encodeToString(encryptedData);
//        Map<String, String> yamlData = new HashMap<>();
//        yamlData.put("encrypted_data", base64EncodedData);
//
//        try (FileWriter writer = new FileWriter(filePath)) {
//            Yaml yaml = new Yaml(getYamlOptions());
//            yaml.dump(yamlData, writer);
//        }
//    }
//
//    private static byte[] loadEncryptedDataFromYaml(String filePath) throws IOException {
//        try (FileReader reader = new FileReader(filePath)) {
//            Yaml yaml = new Yaml();
//            Map<String, String> yamlData = yaml.load(reader);
//            String base64EncodedData = yamlData.get("encrypted_data");
//            return Base64.getDecoder().decode(base64EncodedData);
//        }
//    }
//
//    private static DumperOptions getYamlOptions() {
//        DumperOptions options = new DumperOptions();
//        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
//        return options;
//    }
//}
