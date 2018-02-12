package net.learningpath.callcenter;

import io.vavr.control.Try;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Error;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.dto.response.Success;
import net.learningpath.callcenter.service.DispatcherImpl;

public class Controller {

    private static class ControllerHolder {
        private ControllerHolder() {}
        private static final Controller INSTANCE = new Controller();
    }

    private static Controller getInstance() {
        return ControllerHolder.INSTANCE;
    }

    private Controller() {
    }

    public Response getRequest(Call call) {
        return Try.run(() -> DispatcherImpl.getInstance().dispatchCall(call))
                .failed()
                .map(Error::getInstance)
                .getOrElse(Success::getInstance);
    }

}
