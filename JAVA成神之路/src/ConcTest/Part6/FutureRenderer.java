package ConcTest.Part6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static ConcTest.Part5.Preload.launderThrowable;

public class FutureRenderer {
    private final ExecutorService executor = Executors.newFixedThreadPool(100);

    public void rendererPage(CharSequence source){
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Future<List<ImageData>> future = executor.submit(()-> {
                List<ImageData> result=new ArrayList<>();
                for(ImageInfo info:imageInfos)
                    result.add(info.downloadImage());
                return result;
        });
        rendererText(source);

        try{
            List<ImageData> imageData = future.get();
            for(ImageData data:imageData){
                rendererImage(data);
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            future.cancel(true);
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