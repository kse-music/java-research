package cn.hiboot.java.research.algorithm;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * zt
 *
 * @author DingHao
 * @since 2019/4/3 14:21
 */
public class LeetCodeDemo {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> list = new ArrayList<>();
        LinkedList<TreeNode> data = new LinkedList<>();
        data.offer(root);

        while (!data.isEmpty()) {
            double sum = 0;
            int count = 0;
            int size = data.size();
            for (int i = 0; i < size; i++) {//一层结束
                TreeNode t = data.poll();
                sum += t.val;
                if (t.left != null) {
                    data.offer(t.left);
                }
                if (t.right != null) {
                    data.offer(t.right);
                }
                count++;
            }

            list.add(sum / count);

        }

        return list;
    }

    boolean flag = true;

    public boolean isBalanced(TreeNode root) {
        trav(root);
        return flag;
    }

    int trav(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int l = trav(root.left) + 1;
        int r = trav(root.right) + 1;
        if (l - r > 1 || l - r < -1) {
            flag = false;
        }
        return Math.max(l, r);
    }

    public int calPoints(String[] ops) {
        int[] arr = new int[ops.length];
        int i = 0;
        for (String s : ops) {
            switch (s) {
                case "+":
                    arr[i] = arr[i - 1] + arr[i - 2];
                    i++;
                    break;
                case "D":
                    arr[i] = 2 * arr[i - 1];
                    i++;
                    break;
                case "C":
                    arr[i - 1] = 0;
                    i--;
                    break;
                default:
                    arr[i] = Integer.valueOf(s);
                    i++;
            }
        }
        int sum = 0;
        for (int j = 0; j < arr.length; j++) {
            sum += arr[j];
        }
        return sum;

    }

    @Test
    public void averageOfLevels() {
        TreeNode root = new TreeNode(3);
        TreeNode left = new TreeNode(9);
        TreeNode right = new TreeNode(20);
        root.left = left;
        root.right = right;
        right.left = new TreeNode(15);
        TreeNode treeNode = new TreeNode(7);
        treeNode.left = new TreeNode(2);
        right.right = treeNode;

        List<Double> list = averageOfLevels(root);
        System.out.println(list);
        System.out.println(isBalanced(root));
    }


    @Test
    public void sti() {
        System.out.println(myAtoi(" -912834s72332"));
    }

    public int myAtoi(String str) {
        int n = str.length();
        int i = 0;
        while (i < n && str.charAt(i) == ' ') {//开头是空白字符
            i++;
        }
        if (i == n || !((str.charAt(i) == '+') || (str.charAt(i) == '-') || (str.charAt(i) >= '0' && str.charAt(i) <= '9'))) {//开头非+-和0-9数字
            return 0;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (str.charAt(i) == '-') {//下一个字符是-
            stringBuilder.append('-');
            i++;
        } else if (str.charAt(i) == '+') {//下一个字符是+
            i++;
        }
        if (i == n || !(str.charAt(i) >= '0' && str.charAt(i) <= '9')) {//下一个字符不是数字则直接返回0
            return 0;
        }
        while (i < n && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
            stringBuilder.append(str.charAt(i));
            i++;
        }
        try {
            return Integer.valueOf(stringBuilder.toString());
        } catch (Exception e) {
            if (stringBuilder.substring(0, 1).equals("-")) {
                return Integer.MIN_VALUE;
            } else {
                return Integer.MAX_VALUE;
            }
        }
    }

    @Test
    public void buddyStrings() {
        System.out.println(buddyStrings("abc","acb"));
        System.out.println(buddyStrings("acaaba","aaaabc"));
        System.out.println(buddyStrings("aa","aa"));
    }

    public boolean buddyStrings(String A, String B) {
        if (A.equals(B)) {
            for (int i = 0; i < 26; i++) {
                char ch = (char) ('a' + i);
                if (A.indexOf(ch) != A.lastIndexOf(ch)) {
                    return true;
                }
            }
            return false;
        }
        char[] charsA = A.toCharArray();
        char[] charsB = B.toCharArray();
        if (charsA.length != charsB.length) {
            return false;
        }
        int[] indexDifferent = new int[2];
        int k = 0;
        for (int i = 0; i < charsA.length; i++) {
            if (charsA[i] != charsB[i]) {
                try {
                    indexDifferent[k++] = i;
                } catch (IndexOutOfBoundsException e) {
                    return false;
                }
            }
        }
        return k == 2 && charsA[indexDifferent[0]] == charsB[indexDifferent[1]] && charsA[indexDifferent[1]] == charsB[indexDifferent[0]];
    }

    @Test
    public void twoSum() {
        Map<IndexValue, IndexValue> indexValueIndexValueMap = twoSum(new int[]{1, 2, 2, 4, 5, 6, 7}, 9);
        System.out.println(indexValueIndexValueMap);
    }

    static class IndexValue {
        int index;
        int value;

        public IndexValue(int index, int value) {
            this.index = index;
            this.value = value;
        }

    }

    public Map<IndexValue, IndexValue> twoSum(int[] arr, int sum) {
        Map<IndexValue, IndexValue> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] + arr[j] == sum) {
                    map.put(new IndexValue(i, arr[i]), new IndexValue(j, arr[j]));
                }
            }
        }
        return map;
    }

    @Test
    public void addDigits() {
        System.out.println(addDigits(121));
    }

    public int addDigits(int num) {
        return 1 + (num - 1) % 9;
    }

    @Test
    public void addBinary() {
        System.out.println(addBinary("1111", "111"));
    }

    private void fillStr(StringBuilder sb, int len) {
        for (int i = 0; i < len; i++) {
            sb.insert(0, "0");
        }
    }

    public String addBinary(String a, String b) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder(a);
        StringBuilder sb2 = new StringBuilder(b);
        int maxLen = Math.max(a.length(), b.length());
        fillStr(a.length() > b.length() ? sb2 : sb1, Math.abs(a.length() - b.length()));
        boolean j = false;
        for (int i = maxLen - 1; i >= 0; i--) {
            char s1 = sb1.charAt(i);
            char s2 = sb2.charAt(i);
            if (j) {
                if (s1 != s2) {
                    sb.append("0");
                } else {
                    sb.append("1");
                    if (s1 == '0') {
                        j = false;
                    }
                }
            } else {
                if (s1 != s2) {
                    sb.append("1");
                } else {
                    sb.append("0");
                    if (s1 == '1') {
                        j = true;
                    }
                }
            }
        }
        if (j) {
            sb.append("1");
        }
        return sb.reverse().toString();
    }

    @Test
    public void numDupDigitsAtMostN() {
        long s = System.currentTimeMillis();
        System.out.println(numDupDigitsAtMostN(5987490));
        System.out.println(System.currentTimeMillis() - s);
    }

    public int numDupDigitsAtMostN(int N) {
        // Transform N + 1 to arrayList
        List<Integer> L = new ArrayList<>();
        for (int x = N + 1; x > 0; x /= 10) {
            L.add(0, x % 10);
        }

        // Count the number with digits < N
        int res = 0, n = L.size();
        for (int i = 1; i < n; ++i) {
            res += 9 * A(9, i - 1);
        }

        // Count the number with same prefix
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < n; ++i) {
            for (int j = i > 0 ? 0 : 1; j < L.get(i); ++j) {
                if (!seen.contains(j)) {
                    res += A(9 - i, n - i - 1);
                }
            }
            if (seen.contains(L.get(i))) {
                break;
            }
            seen.add(L.get(i));
        }
        return N - res;
    }

    public int A(int m, int n) {
        return n == 0 ? 1 : A(m, n - 1) * (m - n + 1);
    }

}
