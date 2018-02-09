package net.learningpath.callcenter;

import net.learningpath.callcenter.dto.Call;
import net.learningpath.callcenter.service.DispatcherImpl;

public class Controller {

    private static class ControllerHolder {
        private static final Controller INSTANCE = new Controller();
    }

    private static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    private Controller() {
    }

    public void getRequest(Call call) {
        DispatcherImpl.getInstance().dispatchCall(call);
    }

}
