import {HeaderType, HttpMethodType, MediaType} from "./http.ts";
import type {HttpRequestConfig, PathVariables} from "./http_request_config.ts";
import type {ApiResponse} from "./api.ts";

export class HttpClient {
  private constructor() {
  }

  private static async performRequest<T>(method: HttpMethodType, route: string, o?: HttpRequestConfig): Promise<ApiResponse<T>> {
    let body: BodyInit | undefined = undefined;
    const headers: Record<string, string> = {};

    if (o) {
      if (o.route) {
        route = o.route;
      }
      if (o.pathVariables) {
        route = HttpClient.replacePathKeys(route, o.pathVariables);
      }
      if (o.queryParams) {
        route += `?${new URLSearchParams(o.queryParams).toString()}`;
      }
      if (o.body) {
        body = JSON.stringify(o.body);
        headers[HeaderType.CONTENT_TYPE] = MediaType.APPLICATION_JSON;
      }
      if (o.headers) {
        Object.entries(o.headers).forEach(([key, value]) => {
          headers[key] = value;
        });
      }
    }

    const response = await fetch(route, {
      method: method.valueOf(),
      body: body,
      headers: headers,
    });

    const text = await response.text();
    const contentType = response.headers.get(HeaderType.CONTENT_TYPE);

    if (!response.ok) {
      let error: any = {};
      try {
        error = text ? JSON.parse(text) : {};
      } catch (_) {}
      throw {
        status: response.status,
        type: error.type || "UnknownException",
        message: error.message || "Unknown exception",
        time: error.time || new Date()
      };
    }

    let data: T | undefined = undefined;
    if (text && contentType?.includes(MediaType.APPLICATION_JSON)) {
      data = JSON.parse(text);
    }

    return {
      data: data as T,
      status: response.status
    };
  }

  public static async get<T>(route: string, o?: HttpRequestConfig): Promise<ApiResponse<T>> {
    return this.performRequest(HttpMethodType.GET, route, o);
  }

  public static async post<T>(route: string, o?: HttpRequestConfig): Promise<ApiResponse<T>> {
    return this.performRequest(HttpMethodType.POST, route, o);
  }

  public static async put<T>(route: string, o?: HttpRequestConfig): Promise<ApiResponse<T>> {
    return this.performRequest(HttpMethodType.PUT, route, o);
  }

  public static async delete<T>(route: string, o?: HttpRequestConfig): Promise<ApiResponse<T>> {
    return this.performRequest(HttpMethodType.DELETE, route, o);
  }

  private static replacePathKeys(path: string, pathVariables: PathVariables): string {
    return path.replace(/:([a-zA-Z0-9_]+)/g, (_, key): string =>
      key in pathVariables ? pathVariables[key] : `:${key}`);
  }
}