# Polaris Catalog Setup via REST API

This guide walks you through setting up an **Apache Polaris** catalog by interacting with its REST API using `curl`. It covers authentication, catalog creation, and role/permission configuration.

---

## steps to run

```bash
docker compose up -d
```

## 🔐 Step 1: Get Access Token

Use your **client credentials** to get a bearer token:

```bash
curl -i -X POST http://localhost:8181/api/catalog/v1/oauth/tokens \
  -d 'grant_type=client_credentials&client_id=d69694f1e84c4b1c&client_secret=11cffff66afef80b57e84709e5432889&scope=PRINCIPAL_ROLE:ALL'
```

**Sample Response:**

```json
{
  "access_token": "principal:root;password:11cffff66afef80b57e84709e5432889;realm:default-realm;role:ALL",
  "scope": "PRINCIPAL_ROLE:ALL",
  "token_type": "bearer",
  "expires_in": 3600
}
```

---

## 📁 Step 2: Create a Catalog

```bash
curl -i -X POST http://localhost:8181/api/management/v1/catalogs \
  -H "Authorization: Bearer principal:root;password:11cffff66afef80b57e84709e5432889;realm:default-realm;role:ALL" \
  -H 'Accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "polariscatalog",
    "type": "INTERNAL",
    "properties": {
      "default-base-location": "s3://warehouse"
    },
    "storageConfigInfo": {
      "storageType": "S3",
      "roleArn": "arn:aws:iam::123456789012:role/polaris-storage",
      "allowedLocations": ["s3://warehouse"]
    }
  }'
```

---

## 📄 Step 3: List Catalogs

```bash
curl -X GET "http://localhost:8181/api/management/v1/catalogs" \
     -H "Authorization: Bearer principal:root;password:11cffff66afef80b57e84709e5432889;realm:default-realm;role:ALL" \
     -H "Accept: application/json"
```

---

## 👤 Step 4: Create Principal (User)

```bash
curl -X POST "http://localhost:8181/api/management/v1/principals" \
  -H "Authorization: Bearer principal:root;password:11cffff66afef80b57e84709e5432889;realm:default-realm;role:ALL" \
  -H "Content-Type: application/json" \
  -d '{"name": "polarisuser", "type": "user"}'
```

---

## 🧑‍💼 Step 5: Create Principal Role

```bash
curl -X POST "http://localhost:8181/api/management/v1/principal-roles" \
  -H "Authorization: Bearer principal:root;password:11cffff66afef80b57e84709e5432889;realm:default-realm;role:ALL" \
  -H "Content-Type: application/json" \
  -d '{"principalRole": {"name": "polarisuserrole"}}'
```

---

## 🔗 Step 6: Assign Role to User

```bash
curl -X PUT "http://localhost:8181/api/management/v1/principals/polarisuser/principal-roles" \
  -H "Authorization: Bearer principal:root;password:11cffff66afef80b57e84709e5432889;realm:default-realm;role:ALL" \
  -H "Content-Type: application/json" \
  -d '{"principalRole": {"name": "polarisuserrole"}}'
```

---

## 🏷 Step 7: Create Catalog Role

```bash
curl -X POST "http://localhost:8181/api/management/v1/catalogs/polariscatalog/catalog-roles" \
  -H "Authorization: Bearer principal:root;password:11cffff66afef80b57e84709e5432889;realm:default-realm;role:ALL" \
  -H "Content-Type: application/json" \
  -d '{"catalogRole": {"name": "polariscatalogrole"}}'
```

---

## 🔁 Step 8: Assign Catalog Role to Principal Role

```bash
curl -X PUT "http://localhost:8181/api/management/v1/principal-roles/polarisuserrole/catalog-roles/polariscatalog" \
  -H "Authorization: Bearer principal:root;password:11cffff66afef80b57e84709e5432889;realm:default-realm;role:ALL" \
  -H "Content-Type: application/json" \
  -d '{"catalogRole": {"name": "polariscatalogrole"}}'
```

---

## ✅ Step 9: Grant Catalog Privileges

```bash
curl -X PUT "http://localhost:8181/api/management/v1/catalogs/polariscatalog/catalog-roles/polariscatalogrole/grants" \
  -H "Authorization: Bearer principal:root;password:11cffff66afef80b57e84709e5432889;realm:default-realm;role:ALL" \
  -H "Content-Type: application/json" \
  -d '{"grant": {"type": "catalog", "privilege": "CATALOG_MANAGE_CONTENT"}}'
```

---

## ✅ Final Notes

- Replace secrets and tokens as needed.
- Ensure `polaris` is running and accessible at `localhost:8181`.
- S3 bucket (`s3://warehouse`) should be pre-created and accessible.

