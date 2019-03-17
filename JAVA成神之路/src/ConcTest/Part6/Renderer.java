package ConcTest.Part6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import static ConcTest.Part5.Preload.launderThrowable;

public class Renderer {
    private final ExecutorService executor;

    Renderer(ExecutorService executor){
        this.executor = executor;
    }

    public void rendererPage(CharSequence source){
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        CompletionService<ImageData> completionService = new CompletionService<>(executor);
        for(final ImageInfo imageInfo:imageInfos)
            completionService.submit(()->imageInfo.downloadImage());

        rendererText(source);

        try{
            for(int i=0;i<imageInfos.size();i++){
                ImageData data = completionService.take().get();
                rendererImage(data);
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }catch (ExecutionException e){
            throw launderThrowable(e.getCause());
        }
    }
    private void rendererImage(ImageData data){
    }
    private void rendererText(CharSequence source){
    }
    private List<ImageInfo> scanForImageInfo(CharSequence source){
        return null;
    }
}
