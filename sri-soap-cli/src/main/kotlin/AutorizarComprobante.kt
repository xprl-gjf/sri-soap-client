import com.fasterxml.jackson.databind.ObjectMapper
import ec.com.xprl.efacturacion.soap.client.AutorizacionComprobanteProxy
import java.net.URL

class AutorizarComprobante(private val wsdlLocation: URL, private val claveAcesso: String): Command() {
    override fun execute() {
        val proxy = AutorizacionComprobanteProxy(wsdlLocation)
        val respuesta = proxy.autorizacionIndividual(claveAcesso)

        val objectMapper = ObjectMapper()
        objectMapper.writeValue(System.out, respuesta)
        println()
    }
}
