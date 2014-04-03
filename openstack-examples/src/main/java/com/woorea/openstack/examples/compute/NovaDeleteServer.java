package com.woorea.openstack.examples.compute;

import com.woorea.openstack.examples.ExamplesConfiguration;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.nova.Nova;
import com.woorea.openstack.nova.api.ServersResource;
import com.woorea.openstack.nova.model.Servers;

public class NovaDeleteServer {
	public static void main(String[] args) throws InterruptedException {
		Keystone keystone = new Keystone(ExamplesConfiguration.KEYSTONE_AUTH_URL);
		Access access = keystone.tokens().authenticate(new UsernamePassword(ExamplesConfiguration.KEYSTONE_USERNAME, ExamplesConfiguration.KEYSTONE_PASSWORD))
				.withTenantName(ExamplesConfiguration.TENANT_NAME)
				.execute();

		//use the token in the following requests
		keystone.token(access.getToken().getId());

		Nova novaClient = new Nova(ExamplesConfiguration.NOVA_ENDPOINT.concat("/").concat(access.getToken().getTenant().getId()));
		novaClient.token(access.getToken().getId());

		Servers servers = novaClient.servers().list(true).execute();
		if(servers.getList().size() > 0) {
			ServersResource.Delete deleteServer = novaClient.servers().delete(servers.getList().get(0).getId());
			deleteServer.endpoint(ExamplesConfiguration.NOVA_ENDPOINT);
			deleteServer.execute();
		}
	}
}
