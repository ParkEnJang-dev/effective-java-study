import java.util.*;

public class Test {

    public static void main(String[] args) {


        Set<Integer >set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }

        for (int i = 0; i < 3; i++) {
            set.remove(i);
            list.remove(i);
        }
        System.out.println(set);
        System.out.println(list);
        Test.alphabetize("abc", 1);


    }

    /**
     * dkfjkldfj
     * @param s
     * @param A
     * @return
     * @throws Exception
     */
    private static String alphabetize(String s , Integer A) throws Exception {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
    static class Album {
        private Artist artist;
        private int sales;

        public Album(Artist artist, int sales) {
            this.artist = artist;
            this.sales = sales;
        }

        public Artist artist() {
            return artist;
        }

        public int sales() {
            return sales;
        }

        @Override
        public String toString() {
            return Integer.toString(sales);
        }
    }
    static class Artist {
        private String name;

        public Artist(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}