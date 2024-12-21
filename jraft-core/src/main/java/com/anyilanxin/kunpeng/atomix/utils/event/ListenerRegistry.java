/*
 * Copyright 2015-present Open Networking Foundation
 * Copyright © 2024 anyilanxin xuanhongzhou(anyilanxin@aliyun.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anyilanxin.kunpeng.atomix.utils.event;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation of an event sink and a registry capable of tracking listeners and dispatching
 * events to them as part of event sink processing.
 */
public class ListenerRegistry<E extends Event, L extends EventListener<E>>
    implements ListenerService<E, L>, EventSink<E> {

  /** Set of listeners that have registered. */
  protected final Set<L> listeners = new CopyOnWriteArraySet<>();

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public void addListener(final L listener) {
    checkNotNull(listener, "Listener cannot be null");
    listeners.add(listener);
  }

  @Override
  public void removeListener(final L listener) {
    checkNotNull(listener, "Listener cannot be null");
    if (!listeners.remove(listener)) {
      log.warn("Listener {} not registered", listener);
    }
  }

  @Override
  public void process(final E event) {
    for (final L listener : listeners) {
      try {
        if (listener.isRelevant(event)) {
          listener.event(event);
        }
      } catch (final Exception error) {
        reportProblem(event, error);
      }
    }
  }

  /**
   * Reports a problem encountered while processing an event.
   *
   * @param event event being processed
   * @param error error encountered while processing
   */
  protected void reportProblem(final E event, final Throwable error) {
    log.warn("Exception encountered while processing event " + event, error);
  }
}
