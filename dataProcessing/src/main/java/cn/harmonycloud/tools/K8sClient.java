package cn.harmonycloud.tools;

import cn.harmonycloud.metric.Constant;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;


public class K8sClient {
    private final static KubernetesClient K8S_CLIENT = new DefaultKubernetesClient(new ConfigBuilder()

            .withUsername("kubernetes-admin")
            .withClientCertData("LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUM4akNDQWRxZ0F3SUJBZ0lJQzVqa1FGNjJqbXd3RFFZSktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWVGdzB4T1RBek1qY3hNelF6TXpKYUZ3MHlNREF6TWpZeE16UXpNemxhTURReApGekFWQmdOVkJBb1REbk41YzNSbGJUcHRZWE4wWlhKek1Sa3dGd1lEVlFRREV4QnJkV0psY201bGRHVnpMV0ZrCmJXbHVNSUlCSWpBTkJna3Foa2lHOXcwQkFRRUZBQU9DQVE4QU1JSUJDZ0tDQVFFQXRKYkE1Q0syS3FQK0RWRnEKMmxnTTRJaDMwNElTeHhtZkNKWUpWZWQ2YTVIV3BoV3Y0V0dwYzZHYUt5c1MxMFNvMnRZUmg0MGtNTHhmYUFINgpsQWROd1pqRnhhSzRoRlV5K1hrRUpvUHZyQWo2WEFqdVBGVEFLTGl4OVh4ZE01Wnk1bFNLQ2cxV2dUdXorRkZBCjg3Q2FlaHJCcW9EUVBDRk5uSU9sbUFySDY0dW5QekpweVM2YXlhdDQ3QkE5bjhzZXRuTEVaQmQvVHVqTnhJY00KNEFGUmNKQ0tmU2x3d3BKM05nai9KUlRLcnI3T21Oa3piOVdXYXJrSTlURXlQQXd2aHh4dmZ0U1B1cFhKcm1pYgp1MW1jbnNpVU9VZHpVTjFFTEVzTFgxS2xjZXJKTTVuSTNva2UxRVVNczIwMC9HcE4vb3o0eVh6dnRVZzRHaFBOCnVoU0hHUUlEQVFBQm95Y3dKVEFPQmdOVkhROEJBZjhFQkFNQ0JhQXdFd1lEVlIwbEJBd3dDZ1lJS3dZQkJRVUgKQXdJd0RRWUpLb1pJaHZjTkFRRUxCUUFEZ2dFQkFHZGEyV29OMitQcVMvUEdpTVQ2a1FrOXRiR1I3SXFQYVhkTQpzTkRlc0JIRTFEWERHV3VjWlNscnZVMERKUVMxVTJOc0R0azRFdkdac0d3SGw2RzhXeElBaDlLaUtvQjNQc25JCklrNlA4dkhQWi9nekhUU3NKRXc2b2JHQVpFeWtyOFVvNG1xejBlOTBZM093LzRCVkc5d0tYODJlSm1uQkgwalkKSzRrTXUzY1dkTHp4WjZFZ3J3MEhOWmxtT0xIWWVsYlNOMzQxWEorYmp4YlMyMGp0K1k2eXZGTE5TNjNnUEZERAovRVBRL3M5Qk5lMjhNdCtuNWJCY1lrTklxWkRHUmxwb3loUzZaUFp1REIvQjZoL0tGUk9XZU42aVphMW1TaEtwCmxqOHMzVWl3eFR3UVVuQXJ4S21ZaG5ETEU0RXRwRGR4aHFlVjR2QmZadVJyN2g2RGVwOD0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=")
            .withClientKeyData("LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFcFFJQkFBS0NBUUVBdEpiQTVDSzJLcVArRFZGcTJsZ000SWgzMDRJU3h4bWZDSllKVmVkNmE1SFdwaFd2CjRXR3BjNkdhS3lzUzEwU28ydFlSaDQwa01MeGZhQUg2bEFkTndaakZ4YUs0aEZVeStYa0VKb1B2ckFqNlhBanUKUEZUQUtMaXg5WHhkTTVaeTVsU0tDZzFXZ1R1eitGRkE4N0NhZWhyQnFvRFFQQ0ZObklPbG1Bckg2NHVuUHpKcAp5UzZheWF0NDdCQTluOHNldG5MRVpCZC9UdWpOeEljTTRBRlJjSkNLZlNsd3dwSjNOZ2ovSlJUS3JyN09tTmt6CmI5V1dhcmtJOVRFeVBBd3ZoeHh2ZnRTUHVwWEpybWlidTFtY25zaVVPVWR6VU4xRUxFc0xYMUtsY2VySk01bkkKM29rZTFFVU1zMjAwL0dwTi9vejR5WHp2dFVnNEdoUE51aFNIR1FJREFRQUJBb0lCQUIrSmVEUWZEN3NRbW94TwozSjgyNDBETzlFWWVvQkxmR1pQUlI5NFZwNTVqZ0ozT2ljR05ZWjlNNkhTRGVDM1owcmNzelREajZCOE90b2FlCldkbENFVVV1M2RMdTROUlRZS3lLZ3J3TExHN1p2b2NXRkN5N0REZlFVVnV5bFVBdUZQQytjTURpcFdtNk9HcTAKSGVpd3ZibDNOTDBHODlhY1prWmdBeW96TDhpbGRvSEZPZ3p3d0xXZzduZWdLTFAvdjB5cFdSbVU1QVBZRFMwbQo1cko4eFExWWJrRFIwT3JVSUh5S1h4N0s1bmVmbHZic3FiTEVHcEtKc29wOGF5VTdLZFhvY1g1YnVxTTFoNnUrCjd1ZlpCbWM5YW8yQjNDYmJuRERBRUxRaXdwWHFLV2Q2UzBSSml3QzJVS2NIcFBWbkNLSnlHQUwzZklVakdRQ2oKU2l6djVNVUNnWUVBNktIZ2dnQmxyK05OQUEvb3BFMUJHNWV6emxpM2F0WGZQNExFVmMvU3NIdWhaSlkrN3lSZgpDcTAxK0lNK2tVMEZFeDN4VFJoMzRFODdObXovLzJ6VUVQc1BUQitZMHRUM2J5RTQ3TWQ2Q1cxWHRZWXczZ0FjCjMrWE1ZN0s3T0Y4dHd2VVpGMXhzVW0yS1FHMklSS1hhYk9IeFFPYkQxMjNCOUJQTEQvY2VyTDhDZ1lFQXhycVYKUWsyaHdtSVg5MjFhVEtvTjdscUZUVmtEcWtEN1pYbktVdEwrNGprOEdqaHNSS0Q1TnoxRTExSU0zT3g0MUgrTApXWjJWU2JMTnp0d00xaXR2V0JOWklwZjBCdEwxeFJzVENibnJlQlBPdXVQdUxCUytjZEo5UjhkYS9vK1o4Y2tmCmZRcnJJcVZmK2ZEQmNQbDlWZWZxYTZlWCtzTWpRelB1TFpMSFNpY0NnWUVBbi90ak9FUXBxbEk1REFma3g4bFIKTXphYXN4ZVQyK2NGUnRvWlI2SktsSkFSSittamtqYURIZ2FNalVGRThBdjFwM1g2RWpqM2g4MDFQWEtzZ3U1RwowUDYzUUhTaVZxdWJGbFBVb0JGMnZiRHlscVdZU2JQdUo1UVRnYVRTMkN1c2Z0eXJ5c01HdzVFTmVNMWEzMHJuCjhlVllVbmZneXZCeVovYU11WWdMRW1VQ2dZRUF0aUI5ZGp4cmlSZ1pXdHRQTEgrVk1PS28xRkJGK1p5QVNXbXEKc09sS1NzMmxkV3BNWDBFZjVTNHpqeHBWUzRzMDJacmlmUWpjcnFROTFkejVycll6M3dZc1pIWGFZUDZwMnpDSwo3QmtGNGhCVklCYVFTcm5nSjdSK3VHL054d3FZVFd6RGZQZXFzeC9ZcTltbmp3QTNpNmwxdjUvaXV5Qm5pc2xtCitVRlJmM1VDZ1lFQW8wRVdTekJ0WkJLMTY3NTdpa3RBT201VnFxZjFnQ0pTZUdZUStVTkFpRmhsZG5JWncwTloKSUVkeDROQzNPM0lLOENlWGpoLytDQ2plUVpGRFFhY2xHblJrRzNERzNVblc3b0lMV09QNnJoT3Nid1pMdXBhUwpkditWekdUWU53bFZKcjRrdmQwRXJOWW1TOHV2ay9ldHRpbmgwUzRSREVhU0hmM0N5Q1UxTlhZPQotLS0tLUVORCBSU0EgUFJJVkFURSBLRVktLS0tLQo=")


            .withTrustCerts(true).withMasterUrl(Constant.K8S_MASTER_URL).build());

    private K8sClient() {
    }

    public static KubernetesClient getClient() {
        return K8S_CLIENT;
    }

}
