package cc.allio.uno.data.query.stream;

/**
 * 动态Stream创建器
 *
 * @author j.x
 * @date 2023/1/20 11:25
 * @since 1.1.4
 */
public class StreamBuilder<T> {

    CollectionTimeStream<T> stream;

    public StreamBuilder(CollectionTimeStream<T> stream) {
        this.stream = stream;
    }

    /**
     * 添加功能性 sort stream
     *
     * @return StreamBuilder
     */
    public StreamBuilder<T> sort() {
        this.stream = new SortStream<>(stream);
        return this;
    }

    /**
     * 添加功能性增补 stream
     *
     * @return StreamBuilder
     */
    public StreamBuilder<T> supplement() {
        this.stream = new SupplementTimeStream<>(stream);
        return this;
    }

    /**
     * 添加功能性 Outliers Ignore
     *
     * @return StreamBuilder
     */
    public StreamBuilder<T> outliersIgnore() {
        this.stream = new OutliersIgnoreTimeStream<>(stream);
        return this;
    }

    /**
     * 添加功能性 DiluentTime
     *
     * @return StreamBuilder
     */
    public StreamBuilder<T> diluent() {
        this.stream = new DiluentTimeStream<>(stream);
        return this;
    }

    public CollectionTimeStream<T> build() {
        return this.stream;
    }

    public AsyncStream<T> buildAsync() {
        return new AsyncStream<>(this.stream);
    }
}
