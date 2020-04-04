package org.springframework.util;

import org.springframework.lang.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

    /** 默认初始化大小 */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    /** 默认加载因数 */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    /** 默认并发等级 */
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    /** 引用类型 */
    private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;
    /** 最大并发等级 */
    private static final int MAXIMUM_CONCURRENCY_LEVEL = 1 << 16;
    /** 最大分割大小 */
    private static final int MAXIMUM_SEGMENT_SIZE = 1 << 30;

    /**
     * Array of segments indexed using the high order bits from the hash.
     * 使用哈希中的高阶位索引的段数组
     */
    private final Segment[] segments;

    /**
     * When the average number of references per table exceeds this value resize will be attempted.
     * 当每个表的平均引用数超过此值时，将尝试调整大小
     */
    private final float loadFactor;

    /**
     * The reference type: SOFT or WEAK.
     */
    private final ReferenceType referenceType;

    /**
     * The shift value used to calculate the size of the segments array and an index from the hash.
     * 用于计算段数组大小和来自散列的索引的移位值。
     */
    private final int shift;

    /**
     * Late binding entry set.
     */
    @Nullable
    private volatile Set<Map.Entry<K, V>> entrySet;


    /**
     * 构造方法
     */
    public ConcurrentReferenceHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(
            int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {

        Assert.isTrue(initialCapacity >= 0, "Initial capacity must not be negative");
        Assert.isTrue(loadFactor > 0f, "Load factor must be positive");
        Assert.isTrue(concurrencyLevel > 0, "Concurrency level must be positive");
        Assert.notNull(referenceType, "Reference type must not be null");
        this.loadFactor = loadFactor;
        this.shift = calculateShift(concurrencyLevel, MAXIMUM_CONCURRENCY_LEVEL);
        int size = 1 << this.shift;
        this.referenceType = referenceType;
        int roundedUpSegmentCapacity = (int) ((initialCapacity + size - 1L) / size);
        this.segments = (Segment[]) Array.newInstance(Segment.class, size);
        for (int i = 0; i < this.segments.length; i++) {
            this.segments[i] = new Segment(roundedUpSegmentCapacity);
        }
    }

    /**
     * Calculate a shift value that can be used to create a power-of-two value between
     * the specified maximum and minimum values.
     * 计算一个移位值，该值可用于在指定的最大值和最小值之间创建两个值的幂
     * @param minimumValue the minimum value
     * @param maximumValue the maximum value
     * @return the calculated shift (use {@code 1 << shift} to obtain a value)
     */
    protected int calculateShift(int minimumValue, int maximumValue) {
        int shift = 0;
        int value = 1;
        while (value < minimumValue && value < maximumValue) {
            value <<= 1;
            shift++;
        }
        return shift;
    }


    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return null;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {

    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return false;
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {

    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return null;
    }







    /**
     * Various reference types supported by this map.
     * 多种引用类型支持
     */
    public enum ReferenceType {

        /** Use {@link SoftReference}s */
        SOFT,

        /** Use {@link WeakReference}s */
        WEAK
    }

    /**
     * A single segment used to divide the map to allow better concurrent performance
     * 用于分割映射以获得更好的并发性能的单个段
     */
    protected final class Segment extends ReentrantLock{

    }

    /**
     * Strategy class used to manage {@link Reference}s. This class can be overridden if
     * alternative reference types need to be supported.
     * 如果需要支持替代引用类型，则可以重写用于管理引用的策略类
     */
    protected class ReferenceManager {

        private final ReferenceQueue<Entry<K, V>> queue = new ReferenceQueue<>();

    }




}
