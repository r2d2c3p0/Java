package custom.ssl.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;

import custom.ssl.util.ChecksAndValidations;

public class ListCertificates {

	public static void ListCertificatesMain(String Keystore, String Password) throws
			KeyStoreException,
			NoSuchAlgorithmException,
			CertificateException,
			IOException {

		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		FileInputStream in1 = new FileInputStream(Keystore);
		ks.load(in1, Password.toCharArray());
		@SuppressWarnings("rawtypes")
		Enumeration aliasEnumumeration;
		Certificate[] chainCertificates;

		try {
			aliasEnumumeration = ks.aliases();int aNumber =0;
			while (aliasEnumumeration.hasMoreElements()) {
				boolean isAlias = false;
				String cAlias = (String) aliasEnumumeration.nextElement();
				if (ks.isKeyEntry(cAlias)) {
					System.out.println("\t\t|*|Alias (PrivateKey): "+cAlias);
					isAlias = true;
				} else {
					aNumber++;
					System.out.println("\t\t|"+aNumber+"|Alias: "+cAlias);
				}
				X509Certificate cert = (X509Certificate) ks.getCertificate(cAlias);
				System.out.println("\t\t\tSubject name: "+cert.getSubjectDN());
				System.out.println("\t\t\t\tIssued by: "+cert.getIssuerDN());
				System.out.println("\t\t\t\t\tSerial number: "+cert.getSerialNumber().toString());
				System.out.println("\t\t\t\t\tSerial number(HEX):  "+cert.getSerialNumber().toString(16));
				System.out.println("\t\t\t\t\t\tExpires on: "+cert.getNotAfter());
				try {
					for (List<?> SAN: cert.getSubjectAlternativeNames()) {
						System.out.println("\t\t\t\t\t\t\tSAN entry: "+SAN.get(1));
					}
				} catch (Exception e1) {
					System.out.println("\t\t\t\t\t\t\tSAN entries: none");
				}
				if (isAlias) {
					chainCertificates = ks.getCertificateChain(cAlias);
					/* int ccNumber =1; was report incorrect chain number 01/29/2018.
					 * Remove the variable and assigned the for loop variable as the counter.
					 * 
					 */
					for (int ce=1;ce<chainCertificates.length;ce++) {
						X509Certificate certchain = (X509Certificate) chainCertificates[ce];
						System.out.println("\t\t\t[Chain "+ce+"] Subject name: "+certchain.getSubjectDN());
						System.out.println("\t\t\t[Chain "+ce+"] Issued by: "+certchain.getIssuerDN());
						System.out.println("\t\t\t[Chain "+ce+"] Serial number: "+certchain.getSerialNumber().toString());
						System.out.println("\t\t\t[Chain "+ce+"] Serial number(HEX):  "+certchain.getSerialNumber().toString(16));
						System.out.println("\t\t\t[Chain "+ce+"] Expires on: "+certchain.getNotAfter());						
					}
				}
				System.out.println();
			}
		} catch (KeyStoreException e1) {
			System.err.println("\tERROR| ListCertificates.java KeystoreException occured.\n");
		}
	}

}