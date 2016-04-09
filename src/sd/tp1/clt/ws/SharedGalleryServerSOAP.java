
package sd.tp1.clt.ws;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SharedGalleryServerSOAP", targetNamespace = "http://SOAP.svr.tp1.sd/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SharedGalleryServerSOAP {


    /**
     * 
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getListOfAlbums", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.GetListOfAlbums")
    @ResponseWrapper(localName = "getListOfAlbumsResponse", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.GetListOfAlbumsResponse")
    @Action(input = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/getListOfAlbumsRequest", output = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/getListOfAlbumsResponse")
    public List<String> getListOfAlbums();

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getListOfPictures", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.GetListOfPictures")
    @ResponseWrapper(localName = "getListOfPicturesResponse", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.GetListOfPicturesResponse")
    @Action(input = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/getListOfPicturesRequest", output = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/getListOfPicturesResponse")
    public List<String> getListOfPictures(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns byte[]
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getPictureData", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.GetPictureData")
    @ResponseWrapper(localName = "getPictureDataResponse", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.GetPictureDataResponse")
    @Action(input = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/getPictureDataRequest", output = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/getPictureDataResponse")
    public byte[] getPictureData(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "createAlbum", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.CreateAlbum")
    @ResponseWrapper(localName = "createAlbumResponse", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.CreateAlbumResponse")
    @Action(input = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/createAlbumRequest", output = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/createAlbumResponse")
    public String createAlbum(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deleteAlbum", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.DeleteAlbum")
    @ResponseWrapper(localName = "deleteAlbumResponse", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.DeleteAlbumResponse")
    @Action(input = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/deleteAlbumRequest", output = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/deleteAlbumResponse")
    public Boolean deleteAlbum(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "uploadPicture", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.UploadPicture")
    @ResponseWrapper(localName = "uploadPictureResponse", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.UploadPictureResponse")
    @Action(input = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/uploadPictureRequest", output = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/uploadPictureResponse")
    public String uploadPicture(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        byte[] arg2);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "deletePicture", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.DeletePicture")
    @ResponseWrapper(localName = "deletePictureResponse", targetNamespace = "http://SOAP.svr.tp1.sd/", className = "sd.tp1.clt.ws.DeletePictureResponse")
    @Action(input = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/deletePictureRequest", output = "http://SOAP.svr.tp1.sd/SharedGalleryServerSOAP/deletePictureResponse")
    public Boolean deletePicture(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1);

}
