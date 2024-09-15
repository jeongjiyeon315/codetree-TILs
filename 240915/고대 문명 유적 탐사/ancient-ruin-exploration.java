import java.io.*;
import java.util.*;

public class Main {
    static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static BufferedReader br;
    static StringTokenizer st;
    static StringBuilder sb;
    static boolean flag;
    static int K, M, ans, idx, rotate, turnIdx;
    static int[] number;
    static int[][] arr, temp, cal, visited, relics;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static Queue<Point> queue;

    public static void main(String[] args) throws Exception{
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        st = new StringTokenizer(br.readLine());

        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        arr = new int[5][5];

        for(int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < 5; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        number = new int[M];
        st = new StringTokenizer(br.readLine());
        for(int i = 0; i < M; i++) {
            number[i] = Integer.parseInt(st.nextToken());
        }

        turnIdx = 0;
        idx = 0;

        for(int k = 0; k < K; k++) {
            temp = new int[5][5];
            cal = new int[5][5];
            relics = new int[5][5];
            ans = 0;
            rotate = 3;
            flag = false;

            for(int i = 0; i <= 2; i++) {
                for(int j = 0; j <= 2; j++) {
                    calculateAns(i, j, k);
                }
            }

            turnIdx = idx;

            if(flag) {
                calculateNew();
                for(int i = 0; i < 5; i++) {
                    arr[i] = relics[i].clone();
                }

                sb.append(ans).append(" ");
            } else {
                break;
            }
        }
        System.out.println(sb);
    }

    static void calculateAns(int y, int x, int k) {
        for(int i = 0; i < 5; i++) {
            cal[i] = arr[i].clone();
            temp[i] = arr[i].clone();
        }

        for(int r = 0; r < 3; r++) {
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    temp[x+i][y+j] = cal[x+2-j][y+i];
                }
            }

            for(int i = 0; i < 5; i++) {
                cal[i] = temp[i].clone();
            }

            visited = new int[5][5];
            int g = 1;
            int num = 0;

            for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 5; j++) {
                    if(visited[i][j] == 0) {
                        int cnt = find(i, j, g, temp);
                        g++;
                        if(cnt >= 3) {
                            num += cnt;
                        }
                    }
                }
            }

            if((num > ans || (num == ans && rotate > r)) && num != 0) {
                ans = num;
                rotate = r;
                int tidx = turnIdx;
                flag = true;
                for(int i = 0; i < 5; i++) {
                    for(int j = 4; j >= 0; j--) {
                        if(visited[j][i] == -1) {
                            relics[j][i] = number[tidx++];
                        } else {
                            relics[j][i] = temp[j][i];
                        }
                    }
                }

                idx = tidx;
            }
        }
    }
    static int find(int x, int y, int g, int[][] temp) {
        visited[x][y] = g;
        queue = new LinkedList<>();
        queue.offer(new Point(x, y));
        int cnt = 1;
        Queue<Point> tempqueue = new LinkedList<>();
        tempqueue.offer(new Point(x, y));

        while(!queue.isEmpty()) {
            Point p = queue.poll();

            for(int i = 0; i < 4; i++) {
                int nx = p.x+dx[i];
                int ny = p.y+dy[i];

                if(0 <= nx && nx < 5 && 0 <= ny && ny < 5) {
                    if(visited[nx][ny] == 0 && temp[nx][ny] == temp[x][y]) {
                        visited[nx][ny] = g;
                        cnt++;
                        queue.offer(new Point(nx, ny));
                        tempqueue.offer(new Point(nx, ny));
                    }
                }
            }
        }

        if(cnt >= 3) {
            while(!tempqueue.isEmpty()) {
                Point p = tempqueue.poll();
                visited[p.x][p.y] = -1;
            }
        }

        return cnt;
    }

    static void calculateNew() {
        while(true) {
            visited = new int[5][5];
            int g = 1;
            int cal = 0;
            boolean isOnGoing = false;

            for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 5; j++) {
                    if(visited[i][j] == 0) {
                        int cnt = find(i, j, g, relics);
                        g++;
                        if(cnt >= 3) {
                            cal += cnt;
                            isOnGoing = true;
                        }
                    }
                }
            }

            if(!isOnGoing) return;

            ans += cal;
            for(int i = 0; i < 5; i++) {
                for(int j = 4; j >= 0; j--) {
                    if(visited[j][i] == -1) {
                        relics[j][i] = number[turnIdx++];
                    }
                }
            }
        }
    }
}