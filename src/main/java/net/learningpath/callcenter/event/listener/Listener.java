package net.learningpath.callcenter.event.listener;

import net.learningpath.callcenter.event.topic.Topic;

public interface Listener {

    void update();

    void setTopic(Topic topic);

}
