package ConcTest.Part5;

import ConcTest.MyAnnotation.GuardeBy;
import ConcTest.MyAnnotation.ThreadSafe;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

@ThreadSafe
@GuardeBy("Computerable cache")
public class Factorizer implements Servlet {
    final private Computable<BigInteger,BigInteger[]> cache = new Memorizer<>((BigInteger arg)->factor(arg));

    public void service(ServletRequest req, ServletResponse resp){
        try {
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(resp, cache.compute(i));
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }
    private BigInteger extractFromRequest(ServletRequest req){
        System.out.println("get the BigInteger");
        return new BigInteger("100");
    }
    private void encodeIntoResponse(ServletResponse resp,BigInteger[] i)throws IOException{
        System.out.print("put the BigInterge :");
        for(BigInteger j:i)
            System.out.print(j.toString()+"  ");
        System.out.println();
    }
    private BigInteger[] factor(BigInteger arg)throws InterruptedException{
        System.out.println("factor BigInterge "+arg.toString()+" in 3 seconds later");
        Thread.sleep(3000);
        return new BigInteger[]{new BigInteger("1"),new BigInteger(arg.toString())};
    }
    //-------------------------------------------------------------------------------------------------------------
    public void init(ServletConfig var1) throws ServletException{
    }
    public ServletConfig getServletConfig(){
        return null;
    }
    public String getServletInfo(){
        return null;
    }
    public void destroy(){
    }
}
