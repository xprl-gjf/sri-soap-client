import com.fasterxml.jackson.databind.ObjectMapper
import ec.com.xprl.efacturaction.soap.client.EnvioComprobantesProxy
import java.io.File
import java.net.URL

class EnviarComprobante(val wsdlLocation: URL, val comprobanteFilePath: String): Command() {
    override fun execute() {
        val proxy = EnvioComprobantesProxy(wsdlLocation)
        val file = File(comprobanteFilePath)
        val respuesta = proxy.enviarComprobante(file.readBytes())

        val objectMapper = ObjectMapper()
        objectMapper.writeValue(System.out, respuesta)
        println()
    }
}
