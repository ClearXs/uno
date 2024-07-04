package cc.allio.uno.core.util.template.mvel;

import org.mvel2.templates.util.TemplateOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * solution messy code
 *
 * @author j.x
 * @date 2024/5/29 19:30
 * @since 1.1.9
 */
public class CharsetOutputStream implements TemplateOutputStream {

    OutputStreamWriter writer;

    public CharsetOutputStream(OutputStreamWriter writer) {
        this.writer = writer;
    }

    @Override
    public TemplateOutputStream append(char[] chars) {
        try {
            for (char c : chars) {
                writer.write(c);
            }
            return this;
        } catch (IOException ex) {
            throw new RuntimeException("failed to write to stream", ex);
        }
    }

    @Override
    public TemplateOutputStream append(CharSequence c) {
        try {
            for (int i = 0; i < c.length(); ++i) {
                this.writer.write(c.charAt(i));
            }
            return this;
        } catch (IOException ex) {
            throw new RuntimeException("failed to write to stream", ex);
        }
    }
}
