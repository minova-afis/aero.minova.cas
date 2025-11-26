# µCAS Deployment Guide

## Overview

µCAS (Micro Core Application System) is a standalone, database-based CAS service designed for cloud-native deployments. It serves as the backend for next-generation web interfaces, replacing the traditional Java desktop client.

## Container Image

**Registry**: GitHub Container Registry (GHCR)
**Image**: `ghcr.io/minova-afis/aero.minova.mcas`
**Tags**:
- `latest` - Latest stable release
- `snapshot` - Latest development snapshot
- `{version}` - Specific version (e.g., `1.0.0`)

## Kubernetes Deployment

### Customer Deployment Repository

µCAS deployments are managed through GitOps using ArgoCD in the customer deployment repository:
- **Repository**: `com.minova.skytanking.cloud.deployment`
- **Pattern**: Kustomize base + overlays (development/production)
- **Structure**:
  ```
  kustomize/
  ├── base/{namespace}/
  │   ├── {namespace}-mcas-deployment.yml
  │   ├── {namespace}-mcas-service.yml
  │   └── {namespace}-mcas-service-extern.yml
  └── overlays/{environment}/{namespace}/
      ├── {namespace}-mcas-deployment_patch.yml
      └── {namespace}-mcas-service-extern_patch.yml
  ```

### Deployed Locations

#### Berlin (ber) - Development

**Namespace**: `ber`
**Purpose**: Test environment for next-generation web interface
**Database**: Shared with existing SkyTanking applications
**Access**:
- **Internal**: `ber-mcas-service.ber.svc.cluster.local:8084`
- **External**: LoadBalancer (Azure-assigned IP)

**Resources**:
- **Memory Request**: 512Mi
- **Memory Limit**: 1024Mi
- **Replicas**: 1

**Image**: `ghcr.io/minova-afis/aero.minova.mcas:1.0.0`

### Configuration

#### Environment Variables

µCAS uses Spring Boot configuration via environment variables:

**Database Connection:**
```yaml
spring_datasource_url: ${ber-connection-credentials.mssql-server-url}
login_dataSource: "database"
```

**Hibernate/JPA:**
```yaml
spring_jpa_properties_hibernate_ddl_auto: "none"
spring_jpa_properties_hibernate_dialect: "org.hibernate.dialect.SQLServer2016Dialect"
spring_jpa_properties_hibernate_default_schema: "dbo"
```

**Monitoring:**
```yaml
management_endpoints_enabled_by_default: "true"
management_endpoints_web_exposure_include: "*"
```

**Logging:**
```yaml
APP_LOG_ROOT: "/opt/minova-customer-documents/de.minova.skyber.awb/minova/logs/MCAS"
logging_file_path: "/opt/minova-customer-documents/de.minova.skyber.awb/minova/logs/MCAS"
```

#### Secrets

µCAS uses Kubernetes secrets for sensitive configuration:

- **container-registry**: GHCR pull credentials
- **ber-connection-credentials**: Database connection string
  - Key: `mssql-server-url`
  - Format: `jdbc:sqlserver://HOST:PORT;databaseName=DB;user=USER;password=PASSWORD`

### Volumes

µCAS mounts a persistent volume for customer documents:

```yaml
volumeMounts:
  - mountPath: "/opt/minova-customer-documents/"
    name: volume

volumes:
  - name: volume
    persistentVolumeClaim:
      claimName: ber-root-persistent-volume-claim
```

## Monitoring

### Prometheus Metrics

µCAS exposes Prometheus metrics for monitoring:

**Endpoint**: `http://ber-mcas-service:8081/actuator/prometheus`

**Pod Annotations:**
```yaml
prometheus.io/scrape: "true"
prometheus.io/port: "8081"
prometheus.io/path: "/actuator/prometheus"
```

### Health Checks

- **Liveness**: `http://ber-mcas-service:8081/actuator/health/liveness`
- **Readiness**: `http://ber-mcas-service:8081/actuator/health/readiness`

## Ports

- **8084**: Main CAS REST API
- **8081**: Management/Actuator endpoints (metrics, health)

## Deployment Process

### Via ArgoCD (GitOps)

1. **Update Image Version** in overlay patch:
   ```yaml
   # overlays/development/ber/ber-mcas-deployment_patch.yml
   spec:
     template:
       spec:
         containers:
           - name: ber-mcas-container
             image: ghcr.io/minova-afis/aero.minova.mcas:NEW_VERSION
   ```

2. **Commit and Push** to deployment repository

3. **ArgoCD Auto-Sync** detects changes and deploys

4. **Verify Deployment**:
   ```bash
   kubectl get pods -n ber | grep mcas
   kubectl logs -n ber -l app=ber-mcas-deployment --tail=100
   ```

### Manual Deployment (kubectl)

```bash
# From deployment repository root
cd kustomize/overlays/development/ber

# Preview changes
kubectl kustomize .

# Apply to cluster
kubectl apply -k .

# Check status
kubectl get deployment ber-mcas-deployment -n ber
kubectl get svc ber-mcas-service -n ber
kubectl get svc ber-mcas-service-extern -n ber
```

## Troubleshooting

### Check Pod Status
```bash
kubectl get pods -n ber -l app=ber-mcas-deployment
kubectl describe pod -n ber -l app=ber-mcas-deployment
```

### View Logs
```bash
# Recent logs
kubectl logs -n ber -l app=ber-mcas-deployment --tail=100

# Follow logs
kubectl logs -n ber -l app=ber-mcas-deployment -f

# Previous container logs (if pod crashed)
kubectl logs -n ber -l app=ber-mcas-deployment --previous
```

### Common Issues

**Image Pull Errors:**
- Verify `container-registry` secret exists and is valid
- Check image name and tag are correct

**Database Connection Errors:**
- Verify `ber-connection-credentials` secret exists
- Check database server is accessible from cluster
- Validate connection string format

**Out of Memory:**
- Check pod resource usage: `kubectl top pod -n ber -l app=ber-mcas-deployment`
- Adjust memory limits in overlay patch if needed

## Rollback

### Via ArgoCD
1. Go to ArgoCD UI
2. Select application
3. Click "History"
4. Click "Rollback" on previous successful deployment

### Via kubectl
```bash
# View deployment history
kubectl rollout history deployment/ber-mcas-deployment -n ber

# Rollback to previous revision
kubectl rollout undo deployment/ber-mcas-deployment -n ber

# Rollback to specific revision
kubectl rollout undo deployment/ber-mcas-deployment -n ber --to-revision=2
```

## Adding New Deployment Locations

To deploy µCAS to a new namespace/location:

1. **Create Base Configuration** in `kustomize/base/{namespace}/`:
   - `{namespace}-mcas-deployment.yml`
   - `{namespace}-mcas-service.yml`
   - `{namespace}-mcas-service-extern.yml` (if external access needed)
   - Update `kustomization.yml` to include new resources

2. **Create Overlay Patch** in `kustomize/overlays/{environment}/{namespace}/`:
   - `{namespace}-mcas-deployment_patch.yml` (specify image version)
   - `{namespace}-mcas-service-extern_patch.yml` (optional: specify LoadBalancer IP)
   - Update `kustomization.yml` to include new patches

3. **Configure Secrets**:
   - Database connection credentials
   - Container registry credentials
   - Any other environment-specific secrets

4. **Commit and Deploy** via ArgoCD

## Next Steps

- Configure external DNS for LoadBalancer IP
- Set up log aggregation (ELK/Splunk)
- Configure alerting for health check failures
- Document API endpoints for web interface team
