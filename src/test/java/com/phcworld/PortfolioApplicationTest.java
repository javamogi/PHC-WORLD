package com.phcworld;


import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PortfolioApplicationTest {
	
	@Test
	public void encryptTest() throws Exception{
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("test");
		encryptor.setAlgorithm("PBEWithMD5AndDES");
		encryptor.setStringOutputType("base63");
		String encryptedText = encryptor.encrypt("smtp.gmail.com");
		String plainText = encryptor.decrypt(encryptedText);
		
		System.out.println("encryptedText : " + encryptedText);
		System.out.println("plainText : " + plainText);
	}

}
