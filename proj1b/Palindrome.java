public class Palindrome {

    public Deque<Character> wordToDeque(String word){
        Deque<Character> d = new LinkedListDeque<>();
        for (char c: word.toCharArray()) {
            d.addLast(c);
        }
        return d;
    }

    public boolean isPalindrome(String word) {
        return isPalindrome(wordToDeque(word));
    }

    private boolean isPalindrome(Deque<Character> d) {
        if (d.size() <= 1) {
            return true;
        }
        if (!(d.removeFirst() == d.removeLast())) {
            return false;
        }
        return isPalindrome(d);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        char[] d = word.toCharArray();
        int size = d.length;
        for (int i=0; i<size/2; i++) {
            if (!cc.equalChars(d[i], d[size-i-1])) {
                return false;
            }
        }
        return true;
    }

}
