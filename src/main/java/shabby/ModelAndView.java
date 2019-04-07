package shabby;

public class ModelAndView {
Object model;
String view;

ModelAndView() {
}

ModelAndView(Object obj, String view) {
    this.model = obj;
    this.view = view;
}

public Object getModel() {
    return model;
}

public void setModel(Object model) {
    this.model = model;
}

public String getView() {
    return view;
}

public void setView(String view) {
    this.view = view;
}

}
