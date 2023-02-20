package cc.allio.uno.core;

/**
 * 一些常用的字符串
 *
 * @author jiangw
 * @date 2020/12/7 20:58
 * @since 1.0
 */
public interface StringPool {

    String LEFT_SMALL_BRACKETS = "[";
    String RIGHT_SMALL_BRACKETS = "]";
    String AMPERSAND = "&";
    String AND = "AND";
    String WHERE = "WHERE";
    String AT = "@";
    String ASTERISK = "*";
    String STAR = "*";
    String BACK_SLASH = "\\";
    String COLON = ":";
    String COMMA = ",";
    String DASH = "-";
    String DOLLAR = "$";
    String DOT = "\\.";
    String DOTDOT = "..";
    String DOT_CLASS = ".class";
    String DOT_JAVA = ".java";
    String DOT_JS = ".js";
    String DOT_JSP = ".jsp";
    String DOT_MAP_XML = ".map.xml";
    String DOT_XML = ".xml";
    String EMPTY = "";
    String EQUALS = "=";
    String NON_EQUALS = "!=";
    String TRUE = "true";
    String FALSE = "false";
    String SLASH = "/";
    String HASH = "#";
    String HAT = "^";
    String LEFT_BRACE = "{";
    String LEFT_BRACKET = "(";
    String DOUBLE_LEFT_BRACE = "{{";
    String LEFT_CHEV = "<";
    String LEFT_CHEV_EQUAL = "<=";
    String NEWLINE = "\n";
    String NULL = "null";
    String OFF = "off";
    String ON = "on";
    String OR = "or";
    String PERCENT = "%";
    String PIPE = "|";
    String PLUS = "+";
    String QUESTION_MARK = "?";
    String EXCLAMATION_MARK = "!";
    String QUOTE = "\"";
    String RETURN = "\r";
    String TAB = "\t";
    String RETURN_NEW_LINE = "\r\n";
    String RIGHT_BRACE = "}";
    String RIGHT_BRACKET = ")";
    String DOUBLE_RIGHT_BRACE = "}}";
    String RIGHT_CHEV = ">";
    String RIGHT_CHEV_EQUAL = ">=";
    String SEMICOLON = ";";
    String SINGLE_QUOTE = "'";
    String SPACE = " ";
    String LEFT_SQ_BRACKET = "[";
    String RIGHT_SQ_BRACKET = "]";
    String UNDERSCORE = "_";
    String UTF_8 = "UTF-8";
    String GBK = "GBK";
    String GB2312 = "GB2312";
    String GBK18030 = "GBK18030";
    String ISO_8859_1 = "ISO-8859-1";
    String CONTENT_TYPE_UTF_8 = "text/html;charset=UTF-8";
    String Y = "Y";
    String N = "N";
    String YES = "yes";
    String NO = "no";
    String ONE = "1";
    String ZERO = "0";
    String DOLLAR_LEFT_BRACE = "${";
    String HASH_LEFT_BRACE = "#{";
    String LIKE = "LIKE";
    String SPECIAL_REG_EX = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]";
    String TIME_ZONE = "GMT+8";
    String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    String DATE_FORMAT_DATE = "yyyy-MM-dd";
    String DATE_FORMAT_DATETIME_NOSECOND = "yyyy-MM-dd HH:mm";
    String DATE_FORMAT_DATETIME_NOMINUTE = "yyyy-MM-dd HH";
    String DATE_FORMAT_TIME = "HH:mm:ss";
    String DATE_FORMAT_TIME_NOSECOND = "HH:mm";
    String DATE_FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
    String DATE_FORMAT_TIMESTAMP_NOSPLIT = "yyyyMMddHHmmssSSS";
    String DATE_FORMAT_TIMESTAMP_T = "yyyy-MM-dd'T'HH:mm:ss.sssZZ";
    String DATE_FORMAT_MONTH = "yyyy-MM";
    String DATE_FORMAT_SHORT_DATE = "yyyyMMdd";
    String DATE_FORMAT_SHORT_MONTH = "yyyyMM";
    String DATE_FORMAT_YEAR = "yyyy";
    String FIXED = "fixed";
    String UPPER = "upper";
    String LOWER = "lower";
    String TOKEN = "access_token";
    String SALT = "bsk";
    String GET = "get";
    String ATINC = "at";
    String HTTP = "http://";
    String HTTPS = "https://";
    String TCP = "tcp://";
    String MQTT = "mqtt://";


    interface FileType {
        String TEXT = ".txt";

        interface Compressed {
            String DOT_ZIP = ".zip";
            String ZIP = "zip";
            String DOT_RAR = ".rar";
            String RAR = "rar";
            String DOT_7Z = ".7z";
            String _7Z = "7z";
        }

        interface Program {
            String DOT_WAR = ".war";
            String WAR = "war";
            String DOT_JAR = ".jar";
            String JAR = "jar";
            String DOT_CLASS = ".class";
            String CLASS = "class";
            String DOT_JAVA = ".java";
            String JAVA = "java";
            String DOT_JS = ".js";
            String JS = "js";
            String DOT_JSP = ".jsp";
            String JSP = "jsp";
            String DOT_MAP_XML = ".map.xml";
            String MAP_XML = "map.xml";
            String DOT_XML = ".xml";
            String XML = "xml";
            String DOT_JSON = ".json";
            String JSON = "json";
        }

        interface Image {
            String DOT_BMP = ".bmp";
            String BMP = "bmp";
            String DOT_GIF = ".gif";
            String GIF = "gif";
            String DOT_JPG = ".jpg";
            String JPG = "jpg";
            String DOT_JPEG = ".jpeg";
            String JPEG = "jpeg";
            String DOT_PNG = ".png";
            String PNG = "png";
        }

        interface Office {
            String DOT_DOC = ".doc";
            String DOC = "doc";
            String DOT_DOCX = ".docx";
            String DOCX = "docx";
            String DOT_PPT = ".ppt";
            String PPT = "ppt";
            String DOT_PPTX = ".pptx";
            String PPTX = "pptx";
            String DOT_XLS = ".xls";
            String XLS = "xls";
            String DOT_XLSX = ".xlsx";
            String XLSX = "xlsx";
            String DOT_PDF = ".pdf";
            String PDF = "pdf";
        }

        interface Media {
            String FLV = ".flv";
        }
    }

    /**
     * JDK 格式化转换符
     */
    interface FormatterConversion {
        String DECIMAL_INTEGER = "%d";
        String OCTAL_INTEGER = "%o";
        String HEXADECIMAL_INTEGER = "%x";
        String SCIENTIFIC = "%e";
        String GENERAL = "%g";
        String DECIMAL_FLOAT = "%f";
        String HEXADECIMAL_FLOAT = "%a";
        String CHARACTER = "%c";
        String DATE_TIME = "%t";
        String BOOLEAN = "%b";
        String STRING = "%s";
        String HASHCODE = "%h";
        String LINE_SEPARATOR = "%n";
    }
}
