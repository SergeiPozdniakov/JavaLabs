import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyFind {

    static class SearchData {
        private Color color;
        private String searchInput;

        public SearchData(Color color, String searchInput) {
            this.color = color;
            this.searchInput = searchInput;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public String getSearchInput() {
            return searchInput;
        }

        public void setSearchInput(String searchInput) {
            this.searchInput = searchInput;
        }
    }

    public static void main(String[] args) {

        SearchData searchData = createSearchData(args);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = in.readLine()) != null) {
                String outputLine = line.replace(searchData.getSearchInput(),
                        searchData.getColor().getValue() + searchData.getSearchInput() + Color.ANSI_RESET.getValue());
                System.out.println(outputLine);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }

    }

    private static SearchData createSearchData(String[] args) {
        String color;
        String searchInput;
        if (args.length == 2) {
            color = args[0];
            searchInput = args[1];
            checkColor(color);
        } else {
            color = "ANSI_YELLOW";
            searchInput = args.length == 0 ? ".*" : args[0];
        }
        return new SearchData(Color.valueOf(color), searchInput);
    }

    private static void checkColor(String color) {
        for (Color c : Color.values()) {
            if (c.name().equals(color)) {
                return;
            }
        }
        throw new IllegalArgumentException("Not valid color: " + color);
    }

}