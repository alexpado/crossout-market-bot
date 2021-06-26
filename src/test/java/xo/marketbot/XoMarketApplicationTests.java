package xo.marketbot;

import xo.marketbot.library.services.completion.StringScanner;

class XoMarketApplicationTests {

    public static void main(String... args) {

        StringScanner scanner = new StringScanner("-flag --input \"Very Long Input\" and normal --arg test ahah");

        scanner.scan(flag -> System.out.printf("FLAG: %s%n", flag), (key, value) -> System.out.printf("ARG: %s=%s%n", key, value), System.out::println);

    }

}
