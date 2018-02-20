package net.learningpath.callcenter.event.topic;

import net.learningpath.callcenter.event.listener.Listener;

public interface Topic {

    void register(Listener listener);

    void unregister(Listener listener);

    void notifyListeners();

}
