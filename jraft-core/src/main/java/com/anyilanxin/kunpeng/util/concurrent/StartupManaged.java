package com.anyilanxin.kunpeng.util.concurrent;

/**
 * 引导启动
 *
 * @author zxuanhong
 * @date 2024-12-20 10:21
 * @since
 */
public interface StartupManaged<T> {

  String component();

  /**
   * Executes the startup logic
   *
   * @param context the startup context at the start of this step
   * @return future with startup context at the end of this step
   */
  ComposableFuture<T> startup(final T context);

  /**
   * Executes the shutdown logic
   *
   * @param context the shutdown context at the start of this step
   * @return future with the shutdown context at the end of this step.
   */
  ComposableFuture<T> shutdown(final T context);
}
