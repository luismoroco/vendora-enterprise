import {HttpMethodType} from "./http_method_type.ts";
import type {HttpRequestConfig} from "./http_request_config.ts";
import type {ApiResponse} from "./api_response.ts";

export class HttpClient {
  private constructor() {
  }

  private static async performRequest<T>(method: HttpMethodType, route: string, o?: HttpRequestConfig): Promise<ApiResponse<T>> {
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
}