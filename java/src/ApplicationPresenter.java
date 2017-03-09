/**
 * Created by Oisin on 3/9/2017.
 */
public class ApplicationPresenter implements Presenter {

    private View view;

    @Override
    public void setView(View view) {
        this.view = view;

    }

    private View getView() {
        if(view == null) {
            throw new IllegalStateException("The view is not set");
        }
        return view;
    }
}
