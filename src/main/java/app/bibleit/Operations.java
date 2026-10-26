package app.bibleit;

import java.util.LinkedHashMap;
import java.util.Map;

/** REST operation directory generated from the pinned OpenAPI contract. */
final class Operations {
    private Operations() {}
    static final Map<String, Operation> ALL = directory();

    private static Map<String, Operation> directory() {
        var operations = new LinkedHashMap<String, Operation>();

        operations.put("getOpenApi", new Operation("GET", "/openapi.json", "Download the OpenAPI contract"));

        operations.put("clearLiveStack", new Operation("DELETE", "/lives/{live_id}/stack", "Clear the retained scripture stack"));

        operations.put("createLiveAudienceSecret", new Operation("POST", "/lives/{live_id}/audience-secret", "Create a one-time audience secret"));

        operations.put("deleteLiveAudienceSecret", new Operation("DELETE", "/lives/{live_id}/audience-secret", "Remove audience-secret protection"));

        operations.put("getLiveDisplay", new Operation("GET", "/lives/{live_id}/display", "Read the Live display"));

        operations.put("getLivePlayback", new Operation("GET", "/lives/{live_id}/playback", "Read Live playback state"));

        operations.put("getLiveSettings", new Operation("GET", "/lives/{live_id}/settings", "Read retained reference and selected translations"));

        operations.put("getLiveStack", new Operation("GET", "/lives/{live_id}/stack", "Read retained scripture stack"));

        operations.put("getLiveStatistics", new Operation("GET", "/lives/{live_id}/statistics", "Read Live connection and retained-stack statistics"));

        operations.put("popLiveStack", new Operation("POST", "/lives/{live_id}/stack/removals", "Remove a positive count from the retained stack tail"));

        operations.put("pushLiveStack", new Operation("POST", "/lives/{live_id}/stack", "Push retained scripture with optional selected translations"));

        operations.put("replaceLiveDisplay", new Operation("PUT", "/lives/{live_id}/display", "Replace or clear the Live display"));

        operations.put("rotateLiveAudienceSecret", new Operation("PUT", "/lives/{live_id}/audience-secret", "Rotate a one-time audience secret"));

        operations.put("updateLivePlayback", new Operation("PATCH", "/lives/{live_id}/playback", "Update Live playback state"));

        operations.put("updateLiveSettings", new Operation("PATCH", "/lives/{live_id}/settings", "Update retained reference and translations"));

        operations.put("getLiveEvents", new Operation("GET", "/lives/{live_id}/events", "Receive versioned Live events"));

        operations.put("createLive", new Operation("POST", "/organizations/{organization_id}/lives", "Create an organization Live"));

        operations.put("createLiveQueueItem", new Operation("POST", "/lives/{live_id}/queue-items", "Insert a scripture queue item"));

        operations.put("deleteLive", new Operation("DELETE", "/lives/{live_id}", "Delete a Live"));

        operations.put("deleteLiveQueueItem", new Operation("DELETE", "/lives/{live_id}/queue-items/{item_id}", "Remove a queue item"));

        operations.put("getLive", new Operation("GET", "/lives/{live_id}", "Read Live state"));

        operations.put("getLiveQueueItem", new Operation("GET", "/lives/{live_id}/queue-items/{item_id}", "Read a stable queue item"));

        operations.put("listLiveQueueItems", new Operation("GET", "/lives/{live_id}/queue-items", "List ordered Live queue items"));

        operations.put("listLives", new Operation("GET", "/organizations/{organization_id}/lives", "List organization Lives"));

        operations.put("moveLiveWorkspace", new Operation("PUT", "/lives/{live_id}/workspace", "Move a Live to another organization"));

        operations.put("replaceLiveQueueOrder", new Operation("PUT", "/lives/{live_id}/queue-items/order", "Replace queue ordering"));

        operations.put("updateLiveMetadata", new Operation("PATCH", "/lives/{live_id}", "Edit Live metadata"));

        operations.put("authorizeOAuth", new Operation("GET", "/oauth/authorize", "Start registered-client authorization"));

        operations.put("decideOAuthConsent", new Operation("POST", "/oauth/authorize", "Approve or deny OAuth consent"));

        operations.put("disconnectOAuthApp", new Operation("POST", "/settings/connected-apps", "Disconnect an application"));

        operations.put("exchangeOAuthToken", new Operation("POST", "/oauth/token", "Exchange a code or rotate a refresh token"));

        operations.put("getConnectedAppsPage", new Operation("GET", "/settings/connected-apps", "View connected applications"));

        operations.put("getOAuthMetadata", new Operation("GET", "/.well-known/oauth-authorization-server", "Read OAuth authorization-server metadata"));

        operations.put("revokeOAuthToken", new Operation("POST", "/oauth/revoke", "Revoke delegated access and refresh tokens"));

        operations.put("deleteOrganizationLives", new Operation("DELETE", "/organizations/{organization_id}/lives", "Atomically delete all Lives in this workspace"));

        operations.put("deleteOrganizationMember", new Operation("DELETE", "/organizations/{organization_id}/members/{member_id}", "Remove a member"));

        operations.put("getOrganization", new Operation("GET", "/organizations/{organization_id}", "Read organization identity"));

        operations.put("getOrganizationLiveStudio", new Operation("GET", "/organizations/{organization_id}/live-studio", "Read organization Live Studio settings"));

        operations.put("getOrganizationMember", new Operation("GET", "/organizations/{organization_id}/members/{member_id}", "Read an active membership"));

        operations.put("getOrganizationQuotas", new Operation("GET", "/organizations/{organization_id}/quotas", "Read organization limits and usage"));

        operations.put("getPersonalOrganizationApplication", new Operation("GET", "/me/organization-applications/{application_id}", "Read your organization application status"));

        operations.put("getServiceOrganization", new Operation("GET", "/organization", "Discover the service token organization"));

        operations.put("listOrganizationMembers", new Operation("GET", "/organizations/{organization_id}/members", "List active organization memberships"));

        operations.put("listPersonalOrganizations", new Operation("GET", "/me/organizations", "List your memberships and applications"));

        operations.put("readBook", new Operation("GET", "/organizations/{organization_id}/translations/{slug}/books/{book_id}", "Read a book page with source verse identifiers"));

        operations.put("replaceOrganizationLiveStudio", new Operation("PUT", "/organizations/{organization_id}/live-studio", "Replace organization Live Studio settings"));

        operations.put("updateOrganizationMember", new Operation("PATCH", "/organizations/{organization_id}/members/{member_id}", "Change a member role"));

        operations.put("createPersonalLive", new Operation("POST", "/accounts/{account_id}/lives", "Create a personal Reader Live"));

        operations.put("deletePersonalLives", new Operation("DELETE", "/accounts/{account_id}/lives", "Atomically delete all Lives in this workspace"));

        operations.put("disablePersonalTranslation", new Operation("DELETE", "/accounts/{account_id}/translations/{slug}", "Disable a personal translation"));

        operations.put("enablePersonalTranslation", new Operation("PUT", "/accounts/{account_id}/translations/{slug}", "Enable an installed personal translation"));

        operations.put("getPersonalAccount", new Operation("GET", "/accounts/{account_id}", "Read your personal account"));

        operations.put("getPersonalIdentity", new Operation("GET", "/me", "Read your personal identity"));

        operations.put("getPersonalQuotas", new Operation("GET", "/accounts/{account_id}/quotas", "Read your effective personal quotas and current usage"));

        operations.put("getPersonalTranslation", new Operation("GET", "/accounts/{account_id}/translations/{slug}", "Read an personal account library membership"));

        operations.put("listOAuthGrants", new Operation("GET", "/me/oauth-grants", "List your connected OAuth applications"));

        operations.put("listPersonalAccounts", new Operation("GET", "/accounts", "List your personal account"));

        operations.put("listPersonalLives", new Operation("GET", "/accounts/{account_id}/lives", "List your personal Lives"));

        operations.put("listPersonalTokens", new Operation("GET", "/accounts/{account_id}/tokens", "List personal credential metadata"));

        operations.put("listPersonalTranslations", new Operation("GET", "/accounts/{account_id}/translations", "List the personal account translation library"));

        operations.put("readPersonalBook", new Operation("GET", "/accounts/{account_id}/translations/{slug}/books/{book_id}", "Read a book page with source verse identifiers"));

        operations.put("readPersonalChapter", new Operation("GET", "/accounts/{account_id}/translations/{slug}/books/{book_id}/chapters/{chapter}", "Read a chapter"));

        operations.put("readPersonalVerse", new Operation("GET", "/accounts/{account_id}/translations/{slug}/books/{book_id}/chapters/{chapter}/verses/{verse}", "Read a verse"));

        operations.put("revokeOAuthGrant", new Operation("DELETE", "/me/oauth-grants/{grant_id}", "Disconnect your OAuth grant"));

        operations.put("searchPersonalScripture", new Operation("GET", "/accounts/{account_id}/translations/{slug}/search", "Search personal account scripture"));

        operations.put("getPresentationAsset", new Operation("GET", "/presentations/{presentation_id}/assets/{asset_id}", "Read protected presentation asset bytes"));

        operations.put("uploadPresentationAsset", new Operation("POST", "/presentations/{presentation_id}/assets", "Upload a staged presentation asset"));

        operations.put("createPresentationExport", new Operation("POST", "/presentations/{presentation_id}/exports", "Queue a saved-revision presentation export"));

        operations.put("downloadPresentationExport", new Operation("POST", "/presentations/{presentation_id}/exports/{export_id}/downloads", "Consume and download a completed export once"));

        operations.put("getPresentationExport", new Operation("GET", "/presentations/{presentation_id}/exports/{export_id}", "Read export job status"));

        operations.put("listPresentationExports", new Operation("GET", "/presentations/{presentation_id}/exports", "List the requesting service credential’s export jobs"));

        operations.put("createPresentation", new Operation("POST", "/organizations/{organization_id}/presentations", "Create an organization presentation"));

        operations.put("deletePresentation", new Operation("DELETE", "/presentations/{presentation_id}", "Delete a saved presentation"));

        operations.put("getPresentation", new Operation("GET", "/presentations/{presentation_id}", "Read a saved presentation"));

        operations.put("importPresentation", new Operation("POST", "/organizations/{organization_id}/presentation-imports", "Import a portable presentation bundle"));

        operations.put("listPresentations", new Operation("GET", "/organizations/{organization_id}/presentations", "List organization presentations"));

        operations.put("replacePresentation", new Operation("PUT", "/presentations/{presentation_id}", "Replace a saved presentation document"));

        operations.put("readChapter", new Operation("GET", "/organizations/{organization_id}/translations/{slug}/books/{book_id}/chapters/{chapter}", "Read a chapter"));

        operations.put("readVerse", new Operation("GET", "/organizations/{organization_id}/translations/{slug}/books/{book_id}/chapters/{chapter}/verses/{verse}", "Read a verse"));

        operations.put("searchScripture", new Operation("GET", "/organizations/{organization_id}/translations/{slug}/search", "Search organization scripture"));

        operations.put("createServiceToken", new Operation("POST", "/organizations/{organization_id}/service-tokens", "Create a service token"));

        operations.put("getServiceToken", new Operation("GET", "/organizations/{organization_id}/service-tokens/{credential_id}", "Read service-token configuration"));

        operations.put("listServiceTokens", new Operation("GET", "/organizations/{organization_id}/service-tokens", "List service tokens"));

        operations.put("revokeServiceToken", new Operation("DELETE", "/organizations/{organization_id}/service-tokens/{credential_id}", "Revoke a service token"));

        operations.put("rotateServiceToken", new Operation("POST", "/organizations/{organization_id}/service-tokens/{credential_id}/rotations", "Rotate a service-token secret"));

        operations.put("updateServiceToken", new Operation("PATCH", "/organizations/{organization_id}/service-tokens/{credential_id}", "Edit service-token configuration"));

        operations.put("disableOrganizationTranslation", new Operation("DELETE", "/organizations/{organization_id}/translations/{slug}", "Remove a translation from the organization library"));

        operations.put("enableOrganizationTranslation", new Operation("PUT", "/organizations/{organization_id}/translations/{slug}", "Enable an installed translation in the organization library"));

        operations.put("getOrganizationTranslation", new Operation("GET", "/organizations/{organization_id}/translations/{slug}", "Read an organization library membership"));

        operations.put("getTranslation", new Operation("GET", "/translations/{slug}", "Read translation edition metadata"));

        operations.put("getTranslationCatalogueDetails", new Operation("GET", "/translations/{slug}/catalogue", "Read installed chapter and verse catalogue"));

        operations.put("listBooks", new Operation("GET", "/books", "List canonical book definitions"));

        operations.put("listOrganizationTranslations", new Operation("GET", "/organizations/{organization_id}/translations", "List the organization translation library"));

        operations.put("listTranslationBooks", new Operation("GET", "/translations/{slug}/books", "List a translation edition book coverage"));

        operations.put("listTranslationCatalogue", new Operation("GET", "/translations", "List reviewed translation editions"));

        return Map.copyOf(operations);
    }
}
