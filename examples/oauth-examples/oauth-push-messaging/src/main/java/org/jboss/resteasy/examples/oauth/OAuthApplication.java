package org.jboss.resteasy.examples.oauth;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1231 $
 */
public class OAuthApplication extends Application
{
   HashSet<Object> singletons = new HashSet<Object>();

   public OAuthApplication()
   {
      singletons.add(new MessagingService());
      singletons.add(new MessageReceiver());
      singletons.add(new SubscriberReceiver());
   }

   @Override
   public Set<Class<?>> getClasses()
   {
      HashSet<Class<?>> set = new HashSet<Class<?>>();
      return set;
   }

   @Override
   public Set<Object> getSingletons()
   {
      return singletons;  
   }
}
