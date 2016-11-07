/**
 * 
 */
package test.icmp;

import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;

/**
 * @author sung053
 *
 */
public class ICMPTester {
	// the java entry point

	public static void main(final String[] args)

			throws Exception {

		// request

		final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
		request.setHost("www.google.org");

		// repeat a few times
		for (int count = 1; count <= 4; count++) {
			// delegate
			final IcmpPingResponse response = IcmpPingUtil.executePingRequest(request);
			// log
			final String formattedResponse = IcmpPingUtil.formatResponse(response);
			System.out.println(formattedResponse);
			// rest
			Thread.sleep(1000);
		}

	}

}
