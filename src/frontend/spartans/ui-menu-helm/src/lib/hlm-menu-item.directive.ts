import {booleanAttribute, computed, Directive, Input, input, signal} from '@angular/core';
import {hlm} from '@spartan-ng/ui-core';
import {BrnMenuItemDirective} from '@spartan-ng/ui-menu-brain';
import {cva, type VariantProps} from 'class-variance-authority';
import type {ClassValue} from 'clsx';

export const hlmMenuItemVariants = cva(
  'group w-full relative flex cursor-default select-none items-center px-2.5 py-2.5 text-sm capitalize font-medium tracking-[.0625rem]  outline-none transition-colors hover:bg-accent hover:text-accent-foreground focus-visible:bg-accent focus-visible:text-accent-foreground disabled:pointer-events-none disabled:opacity-50',
  {
    variants: {inset: {true: 'pl-8', false: ''}},
    defaultVariants: {inset: false},
  },
);
export type HlmMenuItemVariants = VariantProps<typeof hlmMenuItemVariants>;

@Directive({
  selector: '[hlmMenuItem]',
  standalone: true,
  host: {
    '[class]': '_computedClass()',
  },
  hostDirectives: [
    {
      directive: BrnMenuItemDirective,
      inputs: ['disabled: disabled'],
      outputs: ['triggered: triggered'],
    },
  ],
})
export class HlmMenuItemDirective {
  public readonly userClass = input<ClassValue>('', {alias: 'class'});
  private readonly _inset = signal<boolean>(false);
  protected _computedClass = computed(() => hlm(hlmMenuItemVariants({inset: this._inset()}), this.userClass()));

  @Input({transform: booleanAttribute})
  set inset(value: boolean) {
    this._inset.set(value);
  }
}
