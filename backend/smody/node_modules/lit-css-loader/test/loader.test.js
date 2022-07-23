import path from "path";

import { compile, getCompiler, getModuleSource } from "./helpers/index";

jest.setTimeout(10000);

describe("loader", () => {
  it("should import from lit-element by default", async () => {
    const compiler = getCompiler("./basic.js");
    const stats = await compile(compiler);

    expect(getModuleSource("./basic.css", stats)).toMatchSnapshot("module");
  });

  it("should import from lit when import is set", async () => {
    const config = {
      module: {
        rules: [
          {
            test: /\.css$/i,
            use: [
              {
                loader: path.resolve(__dirname, "../index"),
                options: {
                  import: "lit",
                },
              },
            ],
          },
        ],
      },
    };

    const compiler = getCompiler("./basic.js", {}, config);
    const stats = await compile(compiler);

    expect(getModuleSource("./basic.css", stats)).toMatchSnapshot("module");
  });
});
