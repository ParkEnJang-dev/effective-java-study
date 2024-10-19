package chapter2.item9.min;

import java.io.*;

public class Item9_Main {
    public static void main(String[] args) throws Exception {

        try {
            if (true){
                throw new Exception("예외 발생");
            }

        }finally {
            throw new Exception("예외 발생");
        }

    }

    private static final int BUFFER_SIZE = 8 * 1024;

    static void copy(String src, String dst) throws IOException {

        InputStream in = new FileInputStream(src);

        try {
            OutputStream out = new FileOutputStream(dst);

            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0)
                    out.write(buf, 0, n);
            } finally {
                //여기가 예외시 try 구문에 예외를 잡아 먹는다.
                //catch 를 사용시 구문 하나가 더생겨 코드가 지저분 해진다.
                out.close();
            }

        } finally {
            in.close();
        }
    }

    // try-with-resources - 를 쓰면 코드가 짧아지고 명확해 진다. 그리고 autoCloseable 인터페이스를 구현한 객체를 사용할 수 있고, 자원회수가 간편하다.
    static void copy2(String src, String dst) throws IOException {
        try(InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(src)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        }
    }
}
