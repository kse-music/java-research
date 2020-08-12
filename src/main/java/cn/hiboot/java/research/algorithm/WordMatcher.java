package cn.hiboot.java.research.algorithm;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 最大匹配
 * 逆向最大匹配
 *
 * @author DingHao
 * @since 2019/9/27 10:49
 */
public class WordMatcher {

    private static final int DEFAULT_MAX_LENGTH = 3; //默认最大字符数

    /**
     * 中文词典
     */
    private List<String> dict;

    /**
     * 取词长度
     */
    private int len;
    /**
     * 当前下标
     */
    private int curIndex;

    public WordMatcher(List<String> dict) {
        this(dict, DEFAULT_MAX_LENGTH);
    }

    public WordMatcher(List<String> dict, int maxLength) {
        this.dict = dict;
        this.len = maxLength;
    }

    public List<Word> maxMatching(String sentence) {
        int length = sentence.length();
        List<Word> rs = Lists.newArrayList();
        while (curIndex < length) {
            String currentWord;
            if (curIndex + len > length) {//超出句子最大长度
                currentWord = sentence.substring(curIndex, length);
            } else {
                currentWord = sentence.substring(curIndex, curIndex + len);
            }
            if (dict.contains(currentWord)) {
                rs.add(new Word(currentWord, curIndex));
                curIndex += len;
                len = DEFAULT_MAX_LENGTH;
            } else {
                len--;
            }
            if (len == 0) {//不存在
                curIndex++;
                len = DEFAULT_MAX_LENGTH;
            }
        }
        return rs;
    }

    public List<Word> reverseMaxMatching(String sentence) {
        curIndex = sentence.length();
        List<Word> rs = Lists.newArrayList();
        while (curIndex > 0) {
            String currentWord;
            if (curIndex - len < 0) {
                currentWord = sentence.substring(0, curIndex);
            } else {
                currentWord = sentence.substring(curIndex - len, curIndex);
            }
            if (dict.contains(currentWord)) {
                curIndex -= len;
                rs.add(new Word(currentWord, curIndex < 0 ? 0 : curIndex));
                len = DEFAULT_MAX_LENGTH;
            } else {
                len--;
            }
            if (len == 0) {
                curIndex--;
                len = DEFAULT_MAX_LENGTH;
            }
        }
        return rs;

    }

    class Word {
        private String word;
        private int start;
        private int end;

        public Word(String word, int start) {
            this.word = word;
            this.start = start;
            this.end = start + word.length();
        }

        @Override
        public String toString() {
            return "Word{" +
                    "word='" + word + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

}
