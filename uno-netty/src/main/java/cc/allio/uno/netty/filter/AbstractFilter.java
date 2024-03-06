package cc.allio.uno.netty.filter;

/**
 * screw
 * @author jiangw
 * @date 2020/12/8 17:18
 * @since 1.0
 */
public abstract class AbstractFilter implements Filter {

    @Override
    public Integer weight() {
        return 0;
    }

    @Override
    public String name() {
        return this.getClass().getName();
    }

    /**
     * 权重越小，优先级越高，相同权重按照添加顺序进行添加。
     */
    @Override
    public int compareTo(Filter o) {
        Integer weight = this.weight();
        Integer compareWeight = o.weight();
        return weight > compareWeight ? 1 :
                weight.equals(compareWeight) ? 0 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Filter that = (Filter) o;
        return this.weight().equals(that.weight()) &&
                this.name().equals(that.name());
    }
}
