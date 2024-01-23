package cc.allio.uno.data.orm.dsl.word;

import cc.allio.uno.data.orm.dsl.Func;

import java.util.Map;
import java.util.TreeMap;

/**
 * SQL 相关关键字存储
 *
 * @author jiangwei
 * @date 2023/1/12 16:43
 * @since 1.1.4
 */
public class KeyWords {

    private KeyWords() {
    }

    // Statement
    public static final KeyWord SELECT = new KeyWord("select");
    public static final KeyWord FROM = new KeyWord("from");


    // Function
    public static final KeyWord COUNT = new KeyWord(Func.COUNT_FUNCTION.getName());
    public static final KeyWord MIN = new KeyWord(Func.MIN_FUNCTION.getName());
    public static final KeyWord MAX = new KeyWord(Func.MAX_FUNCTION.getName());
    public static final KeyWord AVG = new KeyWord(Func.AVG_FUNCTION.getName());
    public static final KeyWord SUM = new KeyWord(Func.SUM_FUNCTION.getName());

    // Other
    public static final Distinct DISTINCT = new Distinct();

    // 关键词集合
    private static final Map<String, KeyWord> WORDS = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        //
        WORDS.put(SELECT.caseInsensitive(), SELECT);
        WORDS.put(FROM.caseInsensitive(), FROM);

        // func
        WORDS.put(COUNT.caseInsensitive(), COUNT);
        WORDS.put(MIN.caseInsensitive(), MIN);
        WORDS.put(MAX.caseInsensitive(), MAX);
        WORDS.put(AVG.caseInsensitive(), AVG);
        WORDS.put(SUM.caseInsensitive(), SUM);

        WORDS.put(DISTINCT.caseInsensitive(), DISTINCT);
    }

    /**
     * 获取关键词
     *
     * @param word 关键词
     * @return KeyWord实例 or null
     */
    public static KeyWord get(String word) {
        return WORDS.computeIfAbsent(word, KeyWords::newWord);
    }

    /**
     * 创建一个关键词，并放入关键词集合之中
     *
     * @param word 关键词
     * @return KeyWord
     */
    public static KeyWord newWord(String word) {
        KeyWord keyWord = new KeyWord(word);
        return WORDS.put(word, keyWord);
    }
}
