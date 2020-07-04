package ir.soroushtabesh.hearthstone.util;

import javafx.application.Platform;
import javafx.beans.WeakListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class FXUtil {
    private FXUtil() {
    }

    public static void runLater(Runnable runnable, long millis) {
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(runnable);
        }).start();
    }

    public static Optional<ButtonType> showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    public static void showAlertInfo(String title, String header, String content) {
        showAlert(title, header, content, Alert.AlertType.INFORMATION);
    }

    public static <T extends Parent> void loadFXMLasRoot(T component) {
        FXMLLoader loader = new FXMLLoader();
        loader.setRoot(component);
        loader.setControllerFactory(theClass -> component);
        String fileName = component.getClass().getSimpleName() + ".fxml";
        try {
            loader.load(component.getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static URL getFXMLResource(Class<?> cls) {
        String format = String.format("fxml/%s.fxml", cls.getSimpleName());
        return cls.getResource(format);
    }

    //code from https://stackoverflow.com/a/43914715/2773264
    public static class BindingUtil {

        public static <E, F> void mapContent(ObservableList<F> mapped, ObservableList<? extends E> source,
                                             Function<? super E, ? extends F> mapper) {
            map(mapped, source, mapper);
        }

        private static <E, F> Object map(ObservableList<F> mapped, ObservableList<? extends E> source,
                                         Function<? super E, ? extends F> mapper) {
            final ListContentMapping<E, F> contentMapping = new ListContentMapping<>(mapped, mapper);
            mapped.setAll(source.stream().map(mapper).collect(toList()));
            source.removeListener(contentMapping);
            source.addListener(contentMapping);
            return contentMapping;
        }

        private static class ListContentMapping<E, F> implements ListChangeListener<E>, WeakListener {
            private final WeakReference<List<F>> mappedRef;
            private final Function<? super E, ? extends F> mapper;

            public ListContentMapping(List<F> mapped, Function<? super E, ? extends F> mapper) {
                this.mappedRef = new WeakReference<>(mapped);
                this.mapper = mapper;
            }

            @Override
            public void onChanged(Change<? extends E> change) {
                final List<F> mapped = mappedRef.get();
                if (mapped == null) {
                    change.getList().removeListener(this);
                } else {
                    while (change.next()) {
                        if (change.wasPermutated()) {
                            mapped.subList(change.getFrom(), change.getTo()).clear();
                            mapped.addAll(change.getFrom(), change.getList().subList(change.getFrom(), change.getTo())
                                    .stream().map(mapper).collect(toList()));
                        } else {
                            if (change.wasRemoved()) {
                                mapped.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                            }
                            if (change.wasAdded()) {
                                mapped.addAll(change.getFrom(), change.getAddedSubList()
                                        .stream().map(mapper).collect(toList()));
                            }
                        }
                    }
                }
            }

            @Override
            public boolean wasGarbageCollected() {
                return mappedRef.get() == null;
            }

            @Override
            public int hashCode() {
                final List<F> list = mappedRef.get();
                return (list == null) ? 0 : list.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }

                final List<F> mapped1 = mappedRef.get();
                if (mapped1 == null) {
                    return false;
                }

                if (obj instanceof ListContentMapping) {
                    final ListContentMapping<?, ?> other = (ListContentMapping<?, ?>) obj;
                    final List<?> mapped2 = other.mappedRef.get();
                    return mapped1 == mapped2;
                }
                return false;
            }
        }
    }
}
