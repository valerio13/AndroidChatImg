package edu.galileo.android.androidchat.addMultimedia;

import org.greenrobot.eventbus.Subscribe;

import edu.galileo.android.androidchat.addMultimedia.events.AddMultimediaEvent;
import edu.galileo.android.androidchat.addMultimedia.ui.AddMultimediaView;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.lib.GreenRobotEventBus;

/**
 * Created by avalo.
 */
public class AddMultimediaPresenterImpl implements AddMultimediaPresenter {
    private EventBus eventBus;
    private AddMultimediaView view;
    private AddMultimediaInteractor interactor;

    public AddMultimediaPresenterImpl(AddMultimediaView view) {
        this.view = view;
        this.eventBus = GreenRobotEventBus.getInstance();
        this.interactor = new AddMultimediaInteractorImpl();
    }

    @Override
    public void onShow() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void addContact(String email) {
        if(view != null){
            view.hideInput();
            view.showProgress();
        }
        interactor.execute(email);
    }

    @Override
    @Subscribe
    public void onEventMainThread(AddMultimediaEvent event) {
        if(view != null){
            view.hideProgress();
            view.showInput();

            if(event.isError()){
                view.multimediaNotAdded();
            } else {
                view.mutimediaAdded();
            }
        }
    }
}
