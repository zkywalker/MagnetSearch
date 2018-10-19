package org.zky.tool.magnetsearch.base;

public interface IUIState {

    /**
     * isPaused:是否已经pause. <br/>
     *
     * @return 是否已经pause
     */
    boolean isPaused();

    /**
     * 是否已经destoryed. <br/>
     *
     * @return 是否已经destoryed
     */
    boolean isDestoryed();

    /**
     * Fragmeng是否Detached;对Activity来讲,返回值同isDestoryed(). <br/>
     *
     * @return Detached
     */
    boolean isDetached();

    /**
     * 是否已Stop. <br/>
     *
     * @return 是否已Stop
     */
    boolean isStopped();

    /**
     * Fragment是否被隐藏，对Activity来讲，返回值同isDestoryed(). <br/>
     *
     * @return 返回值同isDestoryed()
     */
    boolean isFragmentHidden();

    /**
     * 判断是否对用户可见，对Activity来讲和isPause()方法返回值相反；
     * 对Fragment来讲和setUserVisibleHint()的参数值一致. <br/>
     *
     * @return 是否对用户可见
     */
    boolean isVisibleToUser();
}
