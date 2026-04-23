# Smart Campus Sensor & Room Management API

*A JAX-RS RESTful Web Service for campus infrastructure management.*

---

## Coursework Report: Conceptual Questions

### Part 1: Service Architecture & Setup

**1. JAX-RS Resource Lifecycle and Data Synchronization**
* **The Default Lifecycle:** By default, the JAX-RS runtime treats resource classes as per-request. This means that a brand-new instance of a resource class (e.g., `DiscoveryResource` or `SensorResource`) is instantiated by the server for every single incoming HTTP request, and it is immediately destroyed once the response is sent. 
* **Impact on Data Synchronization:** Because a new instance is created per request, relying on standard instance variables to store data would result in total data loss between calls. To solve this, our in-memory data structures must be declared as `static` so they belong to the class itself and are shared across all incoming requests.
* **Preventing Race Conditions:** Since multiple clients might hit the API at the exact same millisecond, multiple threads will attempt to read and write to these static structures simultaneously. To prevent race conditions, data corruption, or server crashes, we must use thread-safe collections (such as `ConcurrentHashMap` and `CopyOnWriteArrayList`) instead of standard `HashMap` or `ArrayList`.

**2. HATEOAS and Hypermedia Benefits**
* **Self-Discoverability:** Hypermedia as the Engine of Application State (HATEOAS) is a hallmark of advanced RESTful design because it makes the API entirely self-discoverable. By embedding hypermedia links within the JSON responses (such as our Discovery endpoint returning the direct paths for the "rooms" and "sensors" collections), the server actively guides the client on what actions and navigation paths are available next.
* **Benefits over Static Documentation:** This approach vastly benefits client developers because it decouples the client-side code from hardcoded server URIs. If the backend architecture evolves and a resource path needs to be updated, the client will automatically follow the new URI provided in the hypermedia link. This prevents client applications from breaking and reduces the reliance on constantly updating external API documentation.

### Part 2: Room Management

**1. Returning IDs vs. Full Objects**
* **Bandwidth and Performance:** Returning only room IDs consumes significantly less network bandwidth, which is ideal for clients on slow connections or when fetching massive lists. However, returning full objects requires more bandwidth but provides immediate context without needing subsequent API calls.
* **Client-Side Processing:** If the API returns only IDs, the client must perform additional `GET /{roomId}` requests for every single room to display any meaningful UI (like the room name or capacity), increasing server load and client-side processing overhead. Returning full objects allows the client application to render the complete room directory immediately from a single request. 

**2. Is the DELETE Operation Idempotent?**
* **Yes, it is idempotent.** An HTTP method is idempotent if executing it multiple times leaves the server in the exact same state as executing it once. 
* **Justification:** If a client mistakenly sends the exact same `DELETE /api/v1/rooms/LIB-301` request multiple times, the first request will successfully remove the room and return a `204 No Content`. Subsequent identical requests will search the DataStore, fail to find `LIB-301`, and return a `404 Not Found`. Despite the different status codes returned to the client, the ultimate state of the server remains unchanged (the room is successfully deleted and does not exist), preserving the idempotent nature of the `DELETE` method.
