package e.susmit.business_savepasswords;

import android.content.Context;
import android.widget.Toast;
import com.scottyab.aescrypt.AESCrypt;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Common {
    private HashMap<Character, Character> rtc = new HashMap<Character, Character>(){{
            put('x','W');
            put('X','t');
            put('r','#');
            put('1','}');
            put('h','9');
            put('$','5');
            put('R','B');
            put('2','r');
            put('f','w');
            put('3','e');
            put('H','I');
            put('}','`');
            put('P','+');
            put('t','g');
            put('N','N');
            put('6','a');
            put('I','z');
            put('n','>');
            put('!','C');
            put('#','Q');
            put(';','6');
            put('D','8');
            put('d','<');
            put('E','L');
            put('e','o');
            put('k','Y');
            put('<','{');
            put('*',']');
            put(']','*');
            put('l',',');
            put('-',':');
            put('v','D');
            put('W','p');
            put('9','M');
            put(')','A');
            put('J','2');
            put('y','c');
            put('~','b');
            put('4','s');
            put('j','?');
            put('V','_');
            put('G','7');
            put('0','O');
            put('`','X');
            put('_','$');
            put('u','R');
            put('s','T');
            put('O','Z');
            put('(','V');
            put('B','-');
            put('b','K');
            put('L','[');
            put('a','f');
            put('q','E');
            put(':',';');
            put('c','/');
            put('o','0');
            put('%','U');
            put('i','j');
            put('z','(');
            put('p','!');
            put('[','S');
            put('U','q');
            put('{','3');
            put('Q','H');
            put('?','.');
            put('K','^');
            put('^','v');
            put('M','x');
            put('+','&');
            put('5','1');
            put('g','|');
            put('Y','P');
            put('F','@');
            put('/','m');
            put('|','y');
            put('A','l');
            put('@','d');
            put('w',')');
            put('.','u');
            put('>','h');
            put('m','~');
            put('S','J');
            put('7','k');
            put('C','i');
            put('&','G');
            put(',','4');
            put('8','F');
            put('T','%');
            put('Z','n');
    }};

    private HashMap<Character, Character> ctr=  new HashMap<Character, Character>(){{
            put('W','x');
            put('t','X');
            put('#','r');
            put('}','1');
            put('9','h');
            put('5','$');
            put('B','R');
            put('r','2');
            put('w','f');
            put('e','3');
            put('I','H');
            put('`','}');
            put('+','P');
            put('g','t');
            put('N','N');
            put('a','6');
            put('z','I');
            put('>','n');
            put('C','!');
            put('Q','#');
            put('6',';');
            put('8','D');
            put('<','d');
            put('L','E');
            put('o','e');
            put('Y','k');
            put('{','<');
            put(']','*');
            put('*',']');
            put(',','l');
            put(':','-');
            put('D','v');
            put('p','W');
            put('M','9');
            put('A',')');
            put('2','J');
            put('c','y');
            put('b','~');
            put('s','4');
            put('?','j');
            put('_','V');
            put('7','G');
            put('O','0');
            put('X','`');
            put('$','_');
            put('R','u');
            put('T','s');
            put('Z','O');
            put('V','(');
            put('-','B');
            put('K','b');
            put('[','L');
            put('f','a');
            put('E','q');
            put(';',':');
            put('/','c');
            put('0','o');
            put('U','%');
            put('j','i');
            put('(','z');
            put('!','p');
            put('S','[');
            put('q','U');
            put('3','{');
            put('H','Q');
            put('.','?');
            put('^','K');
            put('v','^');
            put('x','M');
            put('&','+');
            put('1','5');
            put('|','g');
            put('P','Y');
            put('@','F');
            put('m','/');
            put('y','|');
            put('l','A');
            put('d','@');
            put(')','w');
            put('u','.');
            put('h','>');
            put('~','m');
            put('J','S');
            put('k','7');
            put('i','C');
            put('G','&');
            put('4',',');
            put('F','8');
            put('%','T');
            put('n','Z');
    }};

    public String encode(String s) throws GeneralSecurityException {
        // Reverse
        StringBuilder stringBuilder = new StringBuilder(s);
        String reversed = stringBuilder.reverse().toString();
        // Replace Chars
        char[] perform = reversed.toCharArray();
        for (int i = 0; i < reversed.length(); i++)
        {
            String temp = rtc.get(reversed.charAt(i)).toString();
            if (temp == null)
            {
                continue;
            }
            perform[i] = temp.charAt(0);
        }
        return new String(perform);


    }

    public String decode(String s) throws GeneralSecurityException {
        // Replace Chars
        char[] perform = s.toCharArray();
        for (int i = 0; i < s.length(); i++)
        {
            String temp;
            temp = ctr.get(s.charAt(i)).toString();
            if (temp == null)
            {
                continue;
            }
            perform[i] = temp.charAt(0);
        }
        // reverse
        StringBuilder stringBuilder = new StringBuilder(new String(perform));
        String reversed = stringBuilder.reverse().toString();
        return reversed;
    }

    public String encrypt(String s, String secKey) throws GeneralSecurityException {
        String encryptedMsg = AESCrypt.encrypt(secKey, s);
        return encryptedMsg;
    }

    public String decrypt(String s, String secKey) throws GeneralSecurityException{
        String messageAfterDecrypt = AESCrypt.decrypt(secKey, s);
        return messageAfterDecrypt;
    }

    public String enpass(String s) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(s.getBytes());
        byte[] digest = m.digest();
//Decoding
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = '0' + hashtext;

        }
        return hashtext;

    }

    public void done(Context context)
    {
      Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
    }

    public String enCodePass (String s) throws GeneralSecurityException {
        return enpass(encode(s));
    }
}
