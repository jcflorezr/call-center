package net.learningpath.callcenter;

import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.service.DispatcherImpl;

public class Controller {

    private static class ControllerHolder {
        private ControllerHolder() {}
        private static final Controller INSTANCE = new Controller();
    }

    public static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    private Controller() {
    }

    public Response getRequest(Call call) {
        return DispatcherImpl.getInstance().dispatchCall(call);
    }

}
